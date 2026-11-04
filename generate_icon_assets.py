import os
import numpy as np
from PIL import Image, ImageDraw
from skimage import measure

# Source image
ref_path = r'C:\Users\Bhukya Shrikanth\.gemini\antigravity-ide\brain\ca36718d-322a-42df-9f97-cf3d79665e1c\.user_uploaded\media_1792095120251.jpg'
orig_img = Image.open(ref_path).convert('RGB')
arr = np.array(orig_img.convert('L'), dtype=float)

# Extract contours with subpixel precision
black_field = np.clip((120.0 - arr) / 100.0, 0.0, 1.0)
raw_contours = measure.find_contours(black_field, 0.5)

# Simplify contours with RDP tolerance 0.45 px
contours = {}
for i in range(17):
    contours[i] = measure.approximate_polygon(raw_contours[i], tolerance=0.45)

# Contour indices mapping:
# 0: Outer border of rounded square tile (X[263.6, 759.4], Y[241.5, 766.6])
# 1: Inner yellow tile border (X[297.4, 725.5], Y[275.5, 732.6])
# 2: Flame outer body and inner cutout contour (X[408.5, 606.7], Y[331.8, 563.3])
# 3: Flame center droplet (X[472.5, 543.5], Y[474.6, 566.5])
# Letters:
# 7: '#' outer
# 13: '#' hole
# 10: 'r'
# 8: 'e' outer
# 14: 'e' hole
# 11: 'w'
# 4: 'i' dot
# 12: 'i' stem
# 9: 'n'
# 5: 'd' (first) outer
# 15: 'd' (first) hole
# 6: 'd' (second) outer
# 16: 'd' (second) hole

INK_BLACK = "#111111"
REWIND_YELLOW = "#FFC53D"
WARM_CREAM = "#F4EFE6"

INK_RGB = (17, 17, 17)
YELLOW_RGB = (255, 197, 61)
CREAM_RGB = (244, 239, 230)

def pts_to_svg_d(pts, scale=1.0, offset_x=0.0, offset_y=0.0):
    # pts are (y, x)
    return "M " + " L ".join(f"{(p[1] * scale + offset_x):.2f} {(p[0] * scale + offset_y):.2f}" for p in pts) + " Z"

# 1. Master Canonical SVG (1024x1024)
svg_lines = [
    '<?xml version="1.0" encoding="utf-8"?>',
    '<svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 1024 1024" width="1024" height="1024">',
    f'  <!-- Canvas Background -->',
    f'  <rect width="1024" height="1024" fill="{WARM_CREAM}" />',
    f'  <!-- Outer Solid Black Border -->',
    f'  <path d="{pts_to_svg_d(contours[0])}" fill="{INK_BLACK}" />',
    f'  <!-- Inner Yellow Tile -->',
    f'  <path d="{pts_to_svg_d(contours[1])}" fill="{REWIND_YELLOW}" />',
    f'  <!-- Flame Body -->',
    f'  <path d="{pts_to_svg_d(contours[2])}" fill="{INK_BLACK}" />',
    f'  <!-- Flame Center Droplet -->',
    f'  <path d="{pts_to_svg_d(contours[3])}" fill="{INK_BLACK}" />',
    f'  <!-- #rewindd Wordmark Outers -->',
    f'  <g fill="{INK_BLACK}">',
]
for idx in [7, 10, 8, 11, 4, 12, 9, 5, 6]:
    svg_lines.append(f'    <path d="{pts_to_svg_d(contours[idx])}" />')
svg_lines.append('  </g>')
svg_lines.append(f'  <!-- Wordmark Cutouts -->')
svg_lines.append(f'  <g fill="{REWIND_YELLOW}">')
for idx in [13, 14, 15, 16]:
    svg_lines.append(f'    <path d="{pts_to_svg_d(contours[idx])}" />')
svg_lines.append('  </g>')
svg_lines.append('</svg>')

with open('rewindd_logo.svg', 'w', encoding='utf-8') as f:
    f.write('\n'.join(svg_lines))
print('Written rewindd_logo.svg')

# 2. Master Full-Fidelity 512x512 Play Store Icon (PNG)
# Render at high resolution 2048x2048 then downsample with Lanczos for anti-aliasing
hi_res = Image.new('RGB', (2048, 2048), CREAM_RGB)
hi_draw = ImageDraw.Draw(hi_res)

def draw_full_artwork(draw, scale=2.0, offset_x=0.0, offset_y=0.0):
    def tr(c):
        return [(p[1] * scale + offset_x, p[0] * scale + offset_y) for p in c]
    # Border
    draw.polygon(tr(contours[0]), fill=INK_RGB)
    # Tile
    draw.polygon(tr(contours[1]), fill=YELLOW_RGB)
    # Flame
    draw.polygon(tr(contours[2]), fill=INK_RGB)
    # Droplet
    draw.polygon(tr(contours[3]), fill=INK_RGB)
    # Letters
    for idx in [7, 10, 8, 11, 4, 12, 9, 5, 6]:
        draw.polygon(tr(contours[idx]), fill=INK_RGB)
    for idx in [13, 14, 15, 16]:
        draw.polygon(tr(contours[idx]), fill=YELLOW_RGB)

draw_full_artwork(hi_draw, scale=2.0)
playstore_512 = hi_res.resize((512, 512), Image.Resampling.LANCZOS)
playstore_512.save('playstore_icon.png')
print('Written playstore_icon.png (512x512)')

# Also create high-res transparent master tile (without outer cream padding)
# The tile itself is X[263.6, 759.4] (w=495.8), Y[241.5, 766.6] (h=525.1)
# Center of tile is (511.5, 504.1)
tile_hi_res = Image.new('RGBA', (2048, 2048), (0, 0, 0, 0))
tile_draw = ImageDraw.Draw(tile_hi_res)
draw_full_artwork(tile_draw, scale=2.0)
# Crop to tile bounds with a tiny 4px margin
crop_scale = 2.0
bbox = (int(260 * crop_scale), int(238 * crop_scale), int(764 * crop_scale), int(770 * crop_scale))
cropped_tile = tile_hi_res.crop(bbox)

# Fit cropped tile into a square with transparent or rounded background
max_dim = max(cropped_tile.size)
square_tile = Image.new('RGBA', (max_dim + 40, max_dim + 40), (0, 0, 0, 0))
paste_x = (square_tile.size[0] - cropped_tile.size[0]) // 2
paste_y = (square_tile.size[1] - cropped_tile.size[1]) // 2
square_tile.paste(cropped_tile, (paste_x, paste_y))

# 3. Android VectorDrawables (app/src/main/res/drawable/)
os.makedirs('app/src/main/res/drawable', exist_ok=True)

# 3A. ic_launcher_background.xml
background_xml = '''<?xml version="1.0" encoding="utf-8"?>
<vector xmlns:android="http://schemas.android.com/apk/res/android"
    android:width="108dp"
    android:height="108dp"
    android:viewportWidth="108"
    android:viewportHeight="108">
    <path
        android:fillColor="#FFC53D"
        android:pathData="M0,0h108v108h-108z" />
</vector>
'''
with open('app/src/main/res/drawable/ic_launcher_background.xml', 'w', encoding='utf-8') as f:
    f.write(background_xml)
print('Written app/src/main/res/drawable/ic_launcher_background.xml')

# 3B. ic_launcher_foreground.xml (108dp x 108dp viewport)
# Safe zone is 66dp diameter circle centered at (54, 54).
# Flame bounds in original 1024x1024:
# Flame top: 331.8, Flame bottom: 566.5 -> height = 234.7
# Flame left: 408.5, Flame right: 606.7 -> width = 198.2
# Center: X=507.6, Y=449.15
# We want the flame height in 108dp viewport to be ~46dp (comfortably inside 66dp safe circle, radius 33dp).
# Scale factor: 46.0 / 234.7 = 0.196
flame_scale = 46.0 / 234.7
flame_off_x = 54.0 - 507.6 * flame_scale
flame_off_y = 54.0 - 449.15 * flame_scale

def pts_to_vd_path(pts, scale, off_x, off_y):
    # Android VectorDrawable pathData format
    return "M " + " L ".join(f"{(p[1] * scale + off_x):.2f},{(p[0] * scale + off_y):.2f}" for p in pts) + " Z"

flame_body_path = pts_to_vd_path(contours[2], flame_scale, flame_off_x, flame_off_y)
droplet_path = pts_to_vd_path(contours[3], flame_scale, flame_off_x, flame_off_y)

foreground_xml = f'''<?xml version="1.0" encoding="utf-8"?>
<vector xmlns:android="http://schemas.android.com/apk/res/android"
    android:width="108dp"
    android:height="108dp"
    android:viewportWidth="108"
    android:viewportHeight="108">
    <!-- Flame Outer Silhouette -->
    <path
        android:fillColor="{INK_BLACK}"
        android:pathData="{flame_body_path}" />
    <!-- Flame Center Droplet -->
    <path
        android:fillColor="{INK_BLACK}"
        android:pathData="{droplet_path}" />
</vector>
'''
with open('app/src/main/res/drawable/ic_launcher_foreground.xml', 'w', encoding='utf-8') as f:
    f.write(foreground_xml)
print('Written app/src/main/res/drawable/ic_launcher_foreground.xml')

# 3C. ic_launcher_monochrome.xml (for Android 13+ Material You themed icons)
monochrome_xml = f'''<?xml version="1.0" encoding="utf-8"?>
<vector xmlns:android="http://schemas.android.com/apk/res/android"
    android:width="108dp"
    android:height="108dp"
    android:viewportWidth="108"
    android:viewportHeight="108">
    <path
        android:fillColor="#000000"
        android:pathData="{flame_body_path}" />
    <path
        android:fillColor="#000000"
        android:pathData="{droplet_path}" />
</vector>
'''
with open('app/src/main/res/drawable/ic_launcher_monochrome.xml', 'w', encoding='utf-8') as f:
    f.write(monochrome_xml)
print('Written app/src/main/res/drawable/ic_launcher_monochrome.xml')

# 4. Adaptive Icon definitions in app/src/main/res/mipmap-anydpi-v26/
os.makedirs('app/src/main/res/mipmap-anydpi-v26', exist_ok=True)

adaptive_icon_xml = '''<?xml version="1.0" encoding="utf-8"?>
<adaptive-icon xmlns:android="http://schemas.android.com/apk/res/android">
    <background android:drawable="@drawable/ic_launcher_background" />
    <foreground android:drawable="@drawable/ic_launcher_foreground" />
    <monochrome android:drawable="@drawable/ic_launcher_monochrome" />
</adaptive-icon>
'''
with open('app/src/main/res/mipmap-anydpi-v26/ic_launcher.xml', 'w', encoding='utf-8') as f:
    f.write(adaptive_icon_xml)
with open('app/src/main/res/mipmap-anydpi-v26/ic_launcher_round.xml', 'w', encoding='utf-8') as f:
    f.write(adaptive_icon_xml)
print('Written app/src/main/res/mipmap-anydpi-v26/ic_launcher.xml and ic_launcher_round.xml')

# 5. Density-Specific Legacy Mipmap PNGs
# mdpi: 48x48
# hdpi: 72x72
# xhdpi: 96x96
# xxhdpi: 144x144
# xxxhdpi: 192x192

densities = {
    'mipmap-mdpi': 48,
    'mipmap-hdpi': 72,
    'mipmap-xhdpi': 96,
    'mipmap-xxhdpi': 144,
    'mipmap-xxxhdpi': 192
}

for folder, size in densities.items():
    folder_path = os.path.join('app/src/main/res', folder)
    os.makedirs(folder_path, exist_ok=True)
    
    # Square launcher icon: branded rounded-square tile with border and wordmark
    square_img = square_tile.resize((size, size), Image.Resampling.LANCZOS)
    square_img.save(os.path.join(folder_path, 'ic_launcher.png'))
    
    # Round launcher icon: yellow circle with centered flame
    round_canvas = Image.new('RGBA', (size * 4, size * 4), (0, 0, 0, 0))
    r_draw = ImageDraw.Draw(round_canvas)
    # Circle yellow background
    r_draw.ellipse([(0, 0), (size * 4, size * 4)], fill=YELLOW_RGB)
    # Centered flame scaled to fit within circle
    # Flame height in circle: ~60% of diameter
    r_scale = (size * 4 * 0.60) / 234.7
    r_off_x = (size * 4) / 2.0 - 507.6 * r_scale
    r_off_y = (size * 4) / 2.0 - 449.15 * r_scale
    
    def r_tr(c):
        return [(p[1] * r_scale + r_off_x, p[0] * r_scale + r_off_y) for p in c]
    
    r_draw.polygon(r_tr(contours[2]), fill=INK_RGB)
    r_draw.polygon(r_tr(contours[3]), fill=INK_RGB)
    
    # Mask to circle
    circle_mask = Image.new('L', (size * 4, size * 4), 0)
    cm_draw = ImageDraw.Draw(circle_mask)
    cm_draw.ellipse([(0, 0), (size * 4, size * 4)], fill=255)
    
    masked_round = Image.new('RGBA', (size * 4, size * 4), (0, 0, 0, 0))
    masked_round.paste(round_canvas, mask=circle_mask)
    
    round_img = masked_round.resize((size, size), Image.Resampling.LANCZOS)
    round_img.save(os.path.join(folder_path, 'ic_launcher_round.png'))
    print(f'Generated {folder}/ic_launcher.png and ic_launcher_round.png ({size}x{size})')

print('All assets generated successfully!')

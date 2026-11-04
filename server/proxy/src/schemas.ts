import { z } from 'zod';

export const SummarizeRequestSchema = z.object({
  itemId: z.string().optional(),
  title: z.string().min(1).max(500),
  permittedText: z.string().min(100).max(18000),
  userConsent: z.literal(true)
});

export const SummarizeResponseSchema = z.object({
  summary: z.string().min(10),
  keyPoints: z.array(z.string()).min(1).max(7),
  modelId: z.string(),
  promptVersion: z.string()
});

export const SuggestTagsRequestSchema = z.object({
  title: z.string().min(1).max(500),
  permittedText: z.string().min(50).max(18000),
  existingTags: z.array(z.string()).optional(),
  userConsent: z.literal(true)
});

export const SuggestTagsResponseSchema = z.object({
  suggestions: z.array(z.object({
    tag: z.string(),
    confidence: z.number().min(0).max(1),
    reason: z.string().optional()
  })),
  suggestedCollection: z.string().optional().nullable()
});

export const WeeklyReflectionRequestSchema = z.object({
  dateRange: z.string(),
  totalDwellMinutes: z.number().nonnegative(),
  deltaVsPriorWeekPercent: z.number(),
  perAppMinutes: z.record(z.string(), z.number()),
  userConsent: z.literal(true)
});

export const WeeklyReflectionResponseSchema = z.object({
  narrative: z.string().min(20),
  habitWins: z.array(z.string()).min(1)
});

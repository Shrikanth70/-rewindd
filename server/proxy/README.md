# Rewindd Assistive AI Proxy

Secure HTTPS backend proxy for Rewindd client AI features.
Protects upstream API keys (e.g. Groq), validates input lengths, enforces token-bucket rate limits, and validates output JSON schemas.

## Configuration
Set environment variables:
- `GROQ_API_KEY`: API key from console.groq.com
- `PORT`: (Default: 8080)

## Running Locally
```bash
npm install
export GROQ_API_KEY="gsk_..."
npm run dev
```

## Running Tests
```bash
npm test
```

## Cloud Run Deployment
```bash
gcloud run deploy rewindd-ai-proxy \
  --source . \
  --set-secrets="GROQ_API_KEY=groq-api-key:latest" \
  --allow-unauthenticated
```

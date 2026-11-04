import request from 'supertest';
import { app, rateLimiter } from '../src/index';

describe('AI Proxy Service', () => {
  beforeEach(() => {
    rateLimiter.reset();
  });

  it('health check returns ok', async () => {
    const res = await request(app).get('/v1/health');
    expect(res.status).toBe(200);
    expect(res.body.status).toBe('ok');
    expect(res.body.service).toBe('rewindd-ai-proxy');
  });

  it('rejects summarize request without user consent', async () => {
    const res = await request(app)
      .post('/v1/ai/summarize')
      .send({
        title: 'Test Article',
        permittedText: 'Sample text that is long enough to meet the schema length requirements here...',
        userConsent: false
      });
    expect(res.status).toBe(400);
  });

  it('enforces token-bucket rate limits on burst traffic', async () => {
    // 10 requests allowed
    for (let i = 0; i < 10; i++) {
      await request(app).post('/v1/ai/summarize').send({
        title: 'Rate Limit Test',
        permittedText: 'Sample text that is long enough to meet the schema requirements for length...',
        userConsent: true
      });
    }
    // 11th request should be 429
    const res = await request(app).post('/v1/ai/summarize').send({
      title: 'Rate Limit Test',
      permittedText: 'Sample text that is long enough to meet the schema requirements for length...',
      userConsent: true
    });
    expect(res.status).toBe(429);
  });
});

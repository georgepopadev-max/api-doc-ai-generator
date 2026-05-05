import { Project, GeneratedDoc } from '../models/project.model';

// ─── Mock Projects ────────────────────────────────────────────────────────────

export const MOCK_PROJECTS: Project[] = [
  {
    id: 'proj-auth-001',
    name: 'auth-service',
    description: 'Centralized authentication microservice with JWT and OAuth2 support',
    endpointCount: 12,
    status: 'active',
    lastGenerated: '2026-05-03T14:22:00Z',
    aiProvider: 'openai',
    uploads: []
  },
  {
    id: 'proj-ecom-002',
    name: 'ecommerce-api',
    description: 'RESTful API for e-commerce platform — products, orders, payments',
    endpointCount: 34,
    status: 'active',
    lastGenerated: '2026-05-04T09:10:00Z',
    aiProvider: 'anthropic',
    uploads: []
  },
  {
    id: 'proj-pay-003',
    name: 'payment-gateway',
    description: 'Stripe-powered payment gateway with webhook event handling',
    endpointCount: 8,
    status: 'active',
    lastGenerated: '2026-05-02T16:45:00Z',
    aiProvider: 'openai',
    uploads: []
  },
  {
    id: 'proj-inv-004',
    name: 'inventory-ms',
    description: 'Inventory management microservice with real-time stock tracking',
    endpointCount: 19,
    status: 'idle',
    lastGenerated: '2026-04-28T11:30:00Z',
    aiProvider: 'anthropic',
    uploads: []
  },
  {
    id: 'proj-notif-005',
    name: 'notification-service',
    description: 'Multi-channel notification service: email, SMS, push',
    endpointCount: 7,
    status: 'active',
    lastGenerated: '2026-05-01T08:00:00Z',
    aiProvider: 'openai',
    uploads: []
  }
];

// ─── Mock Generated Docs ─────────────────────────────────────────────────────

export const MOCK_DOCS: Record<string, GeneratedDoc[]> = {
  'proj-auth-001': [
    {
      id: 'doc-auth-001-v3',
      projectId: 'proj-auth-001',
      version: 3,
      specYaml: `openapi: 3.0.3
info:
  title: auth-service
  version: 1.0.0
  description: Centralized authentication microservice

paths:
  /auth/login:
    post:
      summary: Authenticate user
      requestBody:
        required: true
        content:
          application/json:
            schema:
              type: object
              properties:
                email:
                  type: string
                  format: email
                password:
                  type: string
      responses:
        '200':
          description: JWT token returned

  /auth/refresh:
    post:
      summary: Refresh JWT token
      responses:
        '200':
          description: New JWT token

  /auth/logout:
    post:
      summary: Invalidate current session
      responses:
        '204':
          description: Session terminated
`,
      generatedAt: '2026-05-03T14:22:00Z',
      modelUsed: 'gpt-4o',
      status: 'complete'
    }
  ],

  'proj-ecom-002': [
    {
      id: 'doc-ecom-002-v1',
      projectId: 'proj-ecom-002',
      version: 1,
      specYaml: `openapi: 3.0.3
info:
  title: ecommerce-api
  version: 1.0.0

paths:
  /products:
    get:
      summary: List all products
      parameters:
        - name: page
          in: query
          schema:
            type: integer
        - name: limit
          in: query
          schema:
            type: integer
      responses:
        '200':
          description: Paginated product list

  /products/{id}:
    get:
      summary: Get product by ID
      parameters:
        - name: id
          in: path
          required: true
          schema:
            type: string
      responses:
        '200':
          description: Product details
        '404':
          description: Not found

  /orders:
    post:
      summary: Create new order
      responses:
        '201':
          description: Order created

  /orders/{id}:
    get:
      summary: Get order by ID
      responses:
        '200':
          description: Order details
`,
      generatedAt: '2026-05-04T09:10:00Z',
      modelUsed: 'gpt-4o',
      status: 'complete'
    }
  ],

  'proj-pay-003': [
    {
      id: 'doc-pay-003-v2',
      projectId: 'proj-pay-003',
      version: 2,
      specYaml: `openapi: 3.0.3
info:
  title: payment-gateway
  version: 1.0.0

paths:
  /payments/charge:
    post:
      summary: Create a charge
      requestBody:
        required: true
        content:
          application/json:
            schema:
              type: object
              properties:
                amount:
                  type: integer
                  description: Amount in cents
                currency:
                  type: string
                  enum: [usd, eur, gbp]
                source:
                  type: string
      responses:
        '200':
          description: Charge created
          content:
            application/json:
              schema:
                type: object
                properties:
                  id:
                    type: string
                  amount:
                    type: integer
                  status:
                    type: string
                    enum: [pending, succeeded, failed]

  /webhooks/stripe:
    post:
      summary: Handle Stripe webhook events
      responses:
        '200':
          description: OK
`,
      generatedAt: '2026-05-02T16:45:00Z',
      modelUsed: 'claude-sonnet-4',
      status: 'complete'
    }
  ],

  'proj-inv-004': [],
  'proj-notif-005': []
};

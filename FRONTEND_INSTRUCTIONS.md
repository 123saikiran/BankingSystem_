# Banking System React Frontend Instructions

## Overview
This repository now includes a React frontend scaffold under `frontend/` that integrates with the existing Spring Boot backend in `mysqlSpringboot/BankServer/`.

The frontend supports:
- Customer CRUD operations via REST endpoints
- Account balance checks, credit, and debit via REST
- WebSocket messaging for balance queries and debit actions

## Backend requirements
1. Start the Spring Boot backend from `mysqlSpringboot/BankServer/`.
2. The backend exposes:
   - REST base path: `http://localhost:8080/bank`
   - WebSocket endpoint: `ws://localhost:8080/websocket`
3. Database configuration is now in-memory H2, so MySQL is not required.
   - H2 JDBC URL: `jdbc:h2:mem:bankdb;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE`
   - H2 console enabled at `/h2-console`
   - No external database host is required for the backend.

## Frontend setup
1. Install Node.js (recommended 18+) and npm.
2. Open a terminal at the project root.
3. Change to the frontend directory:

```bash
cd frontend
```

4. Install dependencies:

```bash
npm install
```

5. Start the React development server:

```bash
npm run dev
```

The frontend will start on `http://localhost:5173`.

## Proxy and connection details
The React dev server is configured to proxy backend requests for local development:
- `/bank/*` → `http://127.0.0.1:8080`
- `/websocket` → `ws://127.0.0.1:8080`

This means the frontend can use relative REST requests and the websocket URL configured in `src/utils/websocket.js`.

## Frontend features
### Customers
- View all customers
- Create a new customer
- Update existing customer details
- Delete a customer
- Load a customer into the form by selecting from the list or by searching by account ID

### Transactions
- Check balance using REST: `/bank/getBalance?id={id}`
- Credit account using REST: `/bank/credit?id={id}&balance={amount}`
- Debit account using REST: `/bank/debit?id={id}&balance={amount}`

### WebSocket
- The frontend connects to `/websocket` using a browser WebSocket.
- Send a numeric account ID to receive a balance response.
- Send a message in the format `accountId-amount` to initiate a debit via websocket.

## Customization
If your backend runs on another host or port, set the environment variables in `frontend/.env` or update `vite.config.js`:

- `VITE_API_BASE_URL` for REST calls
- `VITE_WEBSOCKET_URL` for the websocket connection

## Build for production
To build the React frontend for production:

```bash
cd frontend
npm run build
```

The production assets will be generated in `frontend/dist/`.

## Notes
- The backend currently does not provide explicit CORS configuration, so local development is easiest using the Vite proxy.
- If you deploy the React app separately, ensure the backend allows CORS or serve the frontend from the same origin as the backend.

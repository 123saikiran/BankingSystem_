# BankingSystem_

BankingSystem_ is a small example application that demonstrates a Spring Boot based banking backend (REST + WebSocket) and a React frontend that interacts with it. The application ships with an in-memory H2 database for local development so you can run the server and client without external DB dependencies.

## Project overview
- Backend: Spring Boot application providing REST endpoints for customer management and account operations, plus a WebSocket endpoint for quick interactive balance queries and debit requests.
- Frontend: A lightweight React (Vite) app in `frontend/` that uses the REST API for CRUD and the WebSocket for realtime queries.

## Why WebSockets (short)
WebSockets provide a persistent, low-latency channel between browser and server, which is ideal for realtime interactions like quickly checking an account balance or pushing immediate transaction responses without repeated HTTP request overhead or polling.

## How it's implemented (short)
- The backend exposes a WebSocket endpoint at `/websocket` implemented with the standard Java WebSocket API (`@ServerEndpoint`). Incoming websocket messages are interpreted by the server:
  - Numeric messages (e.g. `123`) are treated as balance queries for account id `123`.
  - Messages in the format `accountId-amount` (e.g. `123-10.5`) attempt a debit operation.
- A REST controller is available at `/bank` providing standard CRUD operations and account actions (`/addCustomer`, `/getAllCustomers`, `/getBalance`, `/credit`, `/debit`, `/deleteCustomer`).
- The frontend demonstrates both REST and WebSocket usage: it uses REST for CRUD and transactions, and the WebSocket console for quick balance/debit interactions.

## Tech stack
- Java 11, Spring Boot (Web, WebSocket, Spring Data JPA)
- H2 in-memory database (development)
- Maven build system
- React + Vite for frontend

## Project structure (high level)
- `mysqlSpringboot/BankServer` — Spring Boot server with REST and WebSocket endpoints
- `mysqlSpringboot/AtmClient` — small Spring Boot client app (uses same WebSocket endpoint)
- `frontend/` — React/Vite frontend scaffold and API/websocket helpers

## Backend endpoints (summary)
- REST base path: `http://localhost:8080/bank`
  - `POST /bank/addCustomer` — create customer (JSON body)
  - `PUT /bank/updateCustomer` — update customer (JSON body)
  - `GET /bank/getAllCustomers` — list customers
  - `GET /bank/getCustomer?id={id}` — get customer by id
  - `GET /bank/getBalance?id={id}` — get balance (REST)
  - `PUT /bank/credit?id={id}&balance={amount}` — credit account
  - `PUT /bank/debit?id={id}&balance={amount}` — debit account
  - `DELETE /bank/deleteCustomer?id={id}` — delete customer
- WebSocket endpoint: `ws://localhost:8080/websocket` (use `wss://` when serving frontend over HTTPS)

## WebSocket message formats
- `123` — send a numeric account id to receive the current balance as a text response.
- `123-10.5` — debit account `123` by amount `10.5`; server responds with a status message.

## Run the project (quick)
1. Start the backend server (BankServer):

```bash
cd mysqlSpringboot/BankServer
mvn spring-boot:run
```

2. (Optional) Start the ATM client application:

```bash
cd mysqlSpringboot/AtmClient
mvn spring-boot:run
```

3. Start the frontend:

```bash
cd frontend
npm install
npm run dev
```

The frontend dev server runs at `http://localhost:5173` and is configured to proxy `/bank` and `/websocket` to the backend on `localhost:8080` for local development.

## H2 in-memory DB
- The backend uses H2 in-memory databases for local development (no MySQL required). H2 consoles are enabled on the servers (see their `application.properties` files) at `/h2-console` while the apps are running.

## Troubleshooting
- WebSocket connection rules: if your frontend is served over HTTPS, the WebSocket URL must use `wss://`. The frontend helper automatically chooses `wss:` when the page is HTTPS. If connection fails, check browser console and backend logs.
- If you see database-related errors, ensure no leftover `target/` build artifacts are interfering; run a clean build: `mvn clean install` in the backend folders.

## Where to look next
- WebSocket server code: `mysqlSpringboot/BankServer/src/main/java/dp/websocket/server/socket/WebSocketServer.java`
- REST controller: `mysqlSpringboot/BankServer/src/main/java/dp/websocket/server/socket/CustomerController.java`
- Frontend entry: `frontend/src/App.jsx` and websocket helper `frontend/src/utils/websocket.js`

If you'd like, I can also add startup scripts, CI run steps, or wire the frontend build artifacts to be served by the Spring Boot app for a single-origin deployment.

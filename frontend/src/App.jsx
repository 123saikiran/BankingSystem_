import { useEffect, useState } from 'react';
import {
  fetchCustomers,
  createCustomer,
  updateCustomer,
  deleteCustomer,
  getCustomer,
  getBalance,
  creditAccount,
  debitAccount
} from './api/backend.js';
import { createWebSocket } from './utils/websocket.js';

function App() {
  const [customers, setCustomers] = useState([]);
  const [selectedCustomer, setSelectedCustomer] = useState({ id: '', name: '', email: '', balance: '' });
  const [accountId, setAccountId] = useState('');
  const [amount, setAmount] = useState('');
  const [wsMessage, setWsMessage] = useState('');
  const [wsResponse, setWsResponse] = useState('');
  const [serverMessage, setServerMessage] = useState('');
  const [wsStatus, setWsStatus] = useState('Disconnected');
  const [activeTab, setActiveTab] = useState('customers');
  const [socket, setSocket] = useState(null);

  useEffect(() => {
    loadCustomers();

    const ws = createWebSocket({
      onOpen: () => setWsStatus('Connected'),
      onMessage: event => setWsResponse(event.data),
      onError: () => setWsStatus('Error'),
      onClose: () => setWsStatus('Disconnected')
    });

    setSocket(ws);
    return () => {
      if (ws && ws.readyState === WebSocket.OPEN) {
        ws.close();
      }
    };
  }, []);

  async function loadCustomers() {
    const list = await fetchCustomers().catch(error => {
      setServerMessage(`Failed to load customers: ${error.message}`);
      return [];
    });

    setCustomers(list || []);
  }

  function clearForm() {
    setSelectedCustomer({ id: '', name: '', email: '', balance: '' });
    setServerMessage('');
  }

  async function handleAddCustomer(event) {
    event.preventDefault();
    const payload = {
      name: selectedCustomer.name,
      email: selectedCustomer.email,
      balance: parseFloat(selectedCustomer.balance || 0)
    };

    const result = await createCustomer(payload).catch(error => error.message);
    setServerMessage(result);
    await loadCustomers();
    clearForm();
  }

  async function handleUpdateCustomer(event) {
    event.preventDefault();
    if (!selectedCustomer.id) {
      setServerMessage('Please select an existing customer to update.');
      return;
    }

    const payload = {
      id: parseInt(selectedCustomer.id, 10),
      name: selectedCustomer.name,
      email: selectedCustomer.email,
      balance: parseFloat(selectedCustomer.balance || 0)
    };

    const result = await updateCustomer(payload).catch(error => error.message);
    setServerMessage(result);
    await loadCustomers();
    clearForm();
  }

  async function handleDeleteCustomer() {
    if (!selectedCustomer.id) {
      setServerMessage('Select a customer to delete.');
      return;
    }

    const result = await deleteCustomer(selectedCustomer.id).catch(error => error.message);
    setServerMessage(result);
    await loadCustomers();
    clearForm();
  }

  async function handleSearchCustomer() {
    if (!accountId) {
      setServerMessage('Enter a customer id to search.');
      return;
    }

    const result = await getCustomer(accountId).catch(error => {
      setServerMessage(error.message);
      return null;
    });

    if (result && result.id) {
      setSelectedCustomer({
        id: result.id || '',
        name: result.name || '',
        email: result.email || '',
        balance: result.balance || ''
      });
      setServerMessage('Customer loaded into the form.');
    } else {
      setServerMessage('Customer not found.');
    }
  }

  async function handleCheckBalance() {
    if (!accountId) {
      setServerMessage('Enter an account ID to check balance.');
      return;
    }
    const result = await getBalance(accountId).catch(error => error.message);
    setServerMessage(result);
  }

  async function handleCredit() {
    if (!accountId || !amount) {
      setServerMessage('Enter account ID and amount to credit.');
      return;
    }
    const result = await creditAccount(accountId, amount).catch(error => error.message);
    setServerMessage(result);
    await loadCustomers();
  }

  async function handleDebit() {
    if (!accountId || !amount) {
      setServerMessage('Enter account ID and amount to debit.');
      return;
    }
    const result = await debitAccount(accountId, amount).catch(error => error.message);
    setServerMessage(result);
    await loadCustomers();
  }

  function handleSendWebSocket(event) {
    event.preventDefault();
    if (!socket || socket.readyState !== WebSocket.OPEN) {
      setWsResponse('WebSocket is not connected.');
      return;
    }
    socket.send(wsMessage);
    setWsMessage('');
  }

  return (
    <div className="app-shell">
      <header>
        <h1>Banking System Frontend</h1>
        <p>WebSocket status: <strong>{wsStatus}</strong></p>
      </header>

      <nav className="tab-nav">
        <button onClick={() => setActiveTab('customers')} className={activeTab === 'customers' ? 'active' : ''}>Customers</button>
        <button onClick={() => setActiveTab('transactions')} className={activeTab === 'transactions' ? 'active' : ''}>Transactions</button>
        <button onClick={() => setActiveTab('websocket')} className={activeTab === 'websocket' ? 'active' : ''}>WebSocket</button>
      </nav>

      <section className="panel">
        {activeTab === 'customers' && (
          <>
            <div className="grid-two">
              <div className="card">
                <h2>Customer form</h2>
                <form onSubmit={selectedCustomer.id ? handleUpdateCustomer : handleAddCustomer}>
                  <label>
                    Account ID
                    <input type="number" value={selectedCustomer.id} onChange={e => setSelectedCustomer({ ...selectedCustomer, id: e.target.value })} placeholder="Leave blank for new customer" />
                  </label>
                  <label>
                    Name
                    <input type="text" value={selectedCustomer.name} onChange={e => setSelectedCustomer({ ...selectedCustomer, name: e.target.value })} required />
                  </label>
                  <label>
                    Email
                    <input type="email" value={selectedCustomer.email} onChange={e => setSelectedCustomer({ ...selectedCustomer, email: e.target.value })} required />
                  </label>
                  <label>
                    Balance
                    <input type="number" step="0.01" value={selectedCustomer.balance} onChange={e => setSelectedCustomer({ ...selectedCustomer, balance: e.target.value })} required />
                  </label>
                  <div className="button-group">
                    <button type="submit">{selectedCustomer.id ? 'Update' : 'Create'}</button>
                    <button type="button" className="danger" onClick={handleDeleteCustomer}>Delete</button>
                    <button type="button" onClick={clearForm}>Clear</button>
                  </div>
                </form>
              </div>

              <div className="card">
                <h2>Customer list</h2>
                <button type="button" onClick={loadCustomers}>Refresh</button>
                <div className="customer-list">
                  {customers.length === 0 ? (
                    <p>No customers found.</p>
                  ) : (
                    customers.map(customer => (
                      <div key={customer.id} className="customer-row" onClick={() => setSelectedCustomer({
                        id: customer.id || '',
                        name: customer.name || '',
                        email: customer.email || '',
                        balance: customer.balance || ''
                      })}>
                        <span>{customer.id}</span>
                        <strong>{customer.name}</strong>
                        <span>${customer.balance}</span>
                      </div>
                    ))
                  )}
                </div>
              </div>
            </div>

            <div className="card">
              <h2>Lookup customer</h2>
              <div className="inline-form">
                <input type="number" value={accountId} onChange={e => setAccountId(e.target.value)} placeholder="Customer ID" />
                <button type="button" onClick={handleSearchCustomer}>Load</button>
              </div>
            </div>
          </>
        )}

        {activeTab === 'transactions' && (
          <div className="card">
            <h2>Account actions</h2>
            <div className="inline-form">
              <input type="number" value={accountId} onChange={e => setAccountId(e.target.value)} placeholder="Account ID" />
              <input type="number" step="0.01" value={amount} onChange={e => setAmount(e.target.value)} placeholder="Amount" />
            </div>
            <div className="button-group">
              <button onClick={handleCheckBalance}>Check balance</button>
              <button onClick={handleCredit}>Credit</button>
              <button onClick={handleDebit}>Debit</button>
            </div>
          </div>
        )}

        {activeTab === 'websocket' && (
          <div className="card">
            <h2>WebSocket console</h2>
            <p>Send a numeric account ID to query balance via websocket.</p>
            <p>Send <code>accountId-amount</code> to debit an account.</p>
            <form onSubmit={handleSendWebSocket} className="inline-form">
              <input type="text" value={wsMessage} onChange={e => setWsMessage(e.target.value)} placeholder="Message for websocket" />
              <button type="submit">Send</button>
            </form>
            <div className="response-box">
              <strong>Server response:</strong>
              <p>{wsResponse || 'Waiting for websocket response...'}</p>
            </div>
          </div>
        )}
      </section>

      <footer>
        <div className="status-panel">
          <p>{serverMessage}</p>
        </div>
      </footer>
    </div>
  );
}

export default App;

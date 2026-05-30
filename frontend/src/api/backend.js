const BASE_URL = import.meta.env.VITE_API_BASE_URL || '';
const JSON_HEADERS = { 'Content-Type': 'application/json' };

async function handleResponse(response) {
  const text = await response.text();
  if (!response.ok) {
    throw new Error(text || response.statusText);
  }
  try {
    return JSON.parse(text);
  } catch {
    return text;
  }
}

export async function fetchCustomers() {
  const response = await fetch(`${BASE_URL}/bank/getAllCustomers`);
  return handleResponse(response);
}

export async function createCustomer(customer) {
  const response = await fetch(`${BASE_URL}/bank/addCustomer`, {
    method: 'POST',
    headers: JSON_HEADERS,
    body: JSON.stringify(customer)
  });
  return handleResponse(response);
}

export async function updateCustomer(customer) {
  const response = await fetch(`${BASE_URL}/bank/updateCustomer`, {
    method: 'PUT',
    headers: JSON_HEADERS,
    body: JSON.stringify(customer)
  });
  return handleResponse(response);
}

export async function deleteCustomer(id) {
  const response = await fetch(`${BASE_URL}/bank/deleteCustomer?id=${encodeURIComponent(id)}`, {
    method: 'DELETE'
  });
  return handleResponse(response);
}

export async function getCustomer(id) {
  const response = await fetch(`${BASE_URL}/bank/getCustomer?id=${encodeURIComponent(id)}`);
  return handleResponse(response);
}

export async function getBalance(id) {
  const response = await fetch(`${BASE_URL}/bank/getBalance?id=${encodeURIComponent(id)}`);
  return handleResponse(response);
}

export async function creditAccount(id, balance) {
  const response = await fetch(`${BASE_URL}/bank/credit?id=${encodeURIComponent(id)}&balance=${encodeURIComponent(balance)}`, {
    method: 'PUT'
  });
  return handleResponse(response);
}

export async function debitAccount(id, balance) {
  const response = await fetch(`${BASE_URL}/bank/debit?id=${encodeURIComponent(id)}&balance=${encodeURIComponent(balance)}`, {
    method: 'PUT'
  });
  return handleResponse(response);
}

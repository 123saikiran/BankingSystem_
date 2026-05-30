export function createWebSocket({ onOpen, onMessage, onError, onClose }) {
  const defaultProtocol = window.location.protocol === 'https:' ? 'wss:' : 'ws:';
  const defaultUrl = `${defaultProtocol}//${window.location.host}/websocket`;
  const url = import.meta.env.VITE_WEBSOCKET_URL || defaultUrl;
  const socket = new WebSocket(url);

  socket.onopen = onOpen || (() => {});
  socket.onmessage = onMessage || (() => {});
  socket.onerror = onError || (() => {});
  socket.onclose = onClose || (() => {});

  return socket;
}

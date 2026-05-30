import { defineConfig } from 'vite';
import react from '@vitejs/plugin-react';

export default defineConfig({
  plugins: [react()],
  server: {
    port: 5173,
    proxy: {
      '/bank': {
        target: 'http://127.0.0.1:8080',
        changeOrigin: true
      },
      '/websocket': {
        target: 'ws://127.0.0.1:8080',
        ws: true
      }
    }
  }
});

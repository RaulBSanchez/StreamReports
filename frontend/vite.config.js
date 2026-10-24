import react from '@vitejs/plugin-react'
import { defineConfig } from 'vite'

// https://vite.dev/config/
export default defineConfig({
  plugins: [react()],
  server: {
    // Send /api requests to the Spring Boot app during development
    proxy: {
      '/api': 'http://localhost:8080',
    },
  },
})

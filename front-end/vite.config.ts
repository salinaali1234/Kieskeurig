// vite.config.ts
import { fileURLToPath, URL } from 'node:url'
import { defineConfig, loadEnv } from 'vite'
import vue from '@vitejs/plugin-vue'
import vueDevTools from 'vite-plugin-vue-devtools'

export default defineConfig(({ mode }) => ({
  define: {
    env: loadEnv(mode, process.cwd(), 'VITE_APP')
  },
  plugins: [
    vue(),
    vueDevTools(),
  ],
  resolve: {
    alias: {
      '@': fileURLToPath(new URL('./src', import.meta.url))
    },
  },
  optimizeDeps: {
    include: ['fetch-intercept'],
    exclude: ['fsevents']
  },
  build: {
    rollupOptions: {
      external: ['whatwg-fetch', 'fsevents']
    }
  }
}))

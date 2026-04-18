import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react'

export default defineConfig({
    plugins: [react()],

    server: {
        proxy: {
            '/genres': 'http://localhost:8080',
            '/halls': 'http://localhost:8080',
            '/movies': 'http://localhost:8080',
            '/sessions': 'http://localhost:8080',
            '/visitors': 'http://localhost:8080',
            '/tickets': 'http://localhost:8080',
        }
    },
    build: {
        outDir: 'X:\Java\cinema\src\main\resources\static',
        emptyOutDir: true,
    }
})

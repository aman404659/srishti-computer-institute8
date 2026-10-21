import { defineConfig } from 'vite';

// Setting base: process.env.VITE_BASE_PATH || './' ensures that all asset URLs,
// CSS files, JS chunks, and images are relative. This enables the app to run
// seamlessly on GitHub Pages (e.g. https://<user>.github.io/<repo>/),
// root domains, Vercel, and Google AI Studio Preview without broken asset paths.
export default defineConfig({
  base: process.env.VITE_BASE_PATH || './',
  build: {
    outDir: 'dist',
    emptyOutDir: true,
  },
  server: {
    port: 3000,
  },
});

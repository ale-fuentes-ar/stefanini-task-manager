/** @type {import('tailwindcss').Config} */
module.exports = {
  content: [
    "./src/**/*.{html,ts}",
  ],
  theme: {
    extend: {
      colors: {
        'stefanini-blue': '#0066FF',
        'status-pending': '#f1c40f',
        'status-progress': '#3498db',
        'status-completed': '#2ecc71',
      }
    },
  },
  plugins: [],
}
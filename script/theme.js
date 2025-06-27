document.addEventListener('DOMContentLoaded', function() {
    const themeToggle = document.getElementById('themeToggle');
    const themeIcon = document.getElementById('themeIcon');
    const themeText = document.getElementById('themeText');
    
    // Check for saved theme preference or use preferred color scheme
    const savedTheme = localStorage.getItem('theme') || 
                      (window.matchMedia('(prefers-color-scheme: dark)').matches ? 'dark' : 'light');
    
    // Apply the saved theme
    if (savedTheme === 'dark') {
        document.body.classList.add('dark-mode');
        themeIcon.textContent = '☀️';
        themeText.textContent = 'Light Mode';
    }
    
    // Theme toggle functionality
    themeToggle.addEventListener('click', function() {
        document.body.classList.toggle('dark-mode');
        
        if (document.body.classList.contains('dark-mode')) {
            localStorage.setItem('theme', 'dark');
            themeIcon.textContent = '☀️';
            themeText.textContent = 'Light Mode';
        } else {
            localStorage.setItem('theme', 'light');
            themeIcon.textContent = '🌙';
            themeText.textContent = 'Dark Mode';
        }
    });
    
    // Listen for changes in OS color scheme preference
    window.matchMedia('(prefers-color-scheme: dark)').addEventListener('change', e => {
        const newColorScheme = e.matches ? 'dark' : 'light';
        if (!localStorage.getItem('theme')) {
            document.body.classList.toggle('dark-mode', newColorScheme === 'dark');
            themeIcon.textContent = newColorScheme === 'dark' ? '☀️' : '🌙';
            themeText.textContent = newColorScheme === 'dark' ? 'Light Mode' : 'Dark Mode';
        }
    });
});
<%@page contentType="text/html" pageEncoding="UTF-8"%>

<style>
    .wiki-header {
        width: 100%;
        background-color: #fff;
        border: 1px solid #ff0000;
        padding: 10px 0;
    }

    .header-container {
        display: flex;
        justify-content: space-between;
        align-items: center;
        max-width: 1200px;
        margin: 0 auto;
        padding: 0 20px;
    }

    .logo-section {
        display: flex;
        align-items: stretch;
        gap: 15px;
    }

    .logo-image {
        width: 40px;
        height: 40px;
    }

    .logo-text {
        color: #333;
        font-size: 1.2rem;
        font-weight: bold;
    }

    .nav-menu {
        display: flex;
        justify-content: center;
        background-color: #ff0000;
        padding: 8px;
        border-radius: 4px;
        border: 1px;
        border-color: blue;
        margin: 0 20px;
    }

    .nav-menu ul {
        display: flex;
        list-style: none;
        gap: 20px;
        margin: 0;
        padding: 0;
    }

    .nav-menu a {
        color: #ffffff;
        text-decoration: none;
        font-size: 0.9rem;
        padding: 5px 10px;
        transition: background-color 0.3s;
    }

    .nav-menu a:hover {
        background-color: rgba(255, 255, 255, 0.1);
        border-radius: 3px;
    }

    .search-section {
        display: flex;
        align-items: center;
        gap: 10px;
    }

    .search-box {
        display: flex;
        align-items: center;
        border: 1px solid #ddd;
        border-radius: 4px;
        padding: 5px 10px;
    }

    .search-input {
        border: none;
        outline: none;
        padding: 5px;
        width: 200px;
    }

    .search-icon {
        color: #666;
        cursor: pointer;
    }
</style>

<header class="wiki-header">
    <div class="header-container">
        <div class="logo-section">
            <img src="img/Emblem_of_Vietnam.png" alt="Wiki Logo" class="logo-image" id="wiki-logo" name="wiki-logo">
            <span class="logo-text" id="logo-text" name="logo-text">CỔNG THÔNG TIN KHÍ TÀI VIỆT NAM</span>
        </div>

        <nav class="nav-menu" id="main-nav" name="main-nav">
            <ul>
                <li><a href="#" id="nav-home" name="nav-home">Home</a></li>
                <li><a href="#" id="nav-articles" name="nav-articles">Articles</a></li>
                <li><a href="#" id="nav-categories" name="nav-categories">Categories</a></li>
                <li><a href="#" id="nav-community" name="nav-community">Community</a></li>
                <li><a href="#" id="nav-contribute" name="nav-contribute">Contribute</a></li>
                <li><a href="#" id="nav-help" name="nav-help">Help</a></li>
                <li><a href="#" id="nav-about" name="nav-about">About</a></li>
                <li><a href="#" id="nav-contact" name="nav-contact">Contact</a></li>
            </ul>
        </nav>

        <div class="search-section">
            <div class="search-box">
                <input type="text" 
                       class="search-input" 
                       id="wiki-search" 
                       name="wiki-search" 
                       placeholder="Search wiki...">
                <span class="search-icon">🔍</span>
            </div>
        </div>
    </div>
</header>

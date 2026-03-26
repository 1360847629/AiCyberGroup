import './header.scss';

import React, { useState, useEffect, useCallback } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import LoadingBar from 'react-redux-loading-bar';

export interface IHeaderProps {
  isAuthenticated: boolean;
  isAdmin: boolean;
  ribbonEnv: string;
  isInProduction: boolean;
  isOpenAPIEnabled: boolean;
  currentLocale: string;
}

const Header = (props: IHeaderProps) => {
  const navigate = useNavigate();
  const [hasScrolledPastBanner, setHasScrolledPastBanner] = useState(true);
  const [activeDropdownId, setActiveDropdownId] = useState<string | null>(null);
  const [darkMode, setDarkMode] = useState(() => {
    const stored = localStorage.getItem('cy-dark-mode');
    if (stored !== null) return stored === 'true';
    return window.matchMedia('(prefers-color-scheme: dark)').matches;
  });

  useEffect(() => {
    document.body.classList.toggle('dark-mode', darkMode);
    localStorage.setItem('cy-dark-mode', String(darkMode));
  }, [darkMode]);

  useEffect(() => {
    const check = () => {
      const banner = document.getElementById('landing-banner');
      if (!banner) {
        setHasScrolledPastBanner(true);
        return;
      }
      setHasScrolledPastBanner(banner.getBoundingClientRect().bottom <= 56);
    };
    window.addEventListener('scroll', check, { passive: true });
    const interval = setInterval(check, 300);
    check();
    return () => {
      window.removeEventListener('scroll', check);
      clearInterval(interval);
    };
  }, []);

  useEffect(() => {
    document.body.classList.toggle('sidebar-visible', !hasScrolledPastBanner);
    document.body.classList.toggle('sidebar-collapsed', hasScrolledPastBanner);
  }, [hasScrolledPastBanner]);

  useEffect(() => {
    const handler = (e: MouseEvent) => {
      if (!(e.target as HTMLElement).closest('.cy-dropdown')) setActiveDropdownId(null);
    };
    document.addEventListener('click', handler);
    return () => document.removeEventListener('click', handler);
  }, []);

  useEffect(() => {
    if (hasScrolledPastBanner && activeDropdownId?.startsWith('sidebar-')) setActiveDropdownId(null);
    if (!hasScrolledPastBanner && activeDropdownId?.startsWith('header-')) setActiveDropdownId(null);
  }, [hasScrolledPastBanner]);

  const toggleDropdown = useCallback((id: string) => {
    setActiveDropdownId(prev => (prev === id ? null : id));
  }, []);

  const goHome = useCallback(
    (e: React.MouseEvent) => {
      e.preventDefault();
      if (props.isAuthenticated) {
        // there's definitely a better way to do this, not sure how though
        navigate('/?page=1&sort=id,asc');
      } else {
        window.scrollTo({ top: 0, behavior: 'smooth' });
      }
    },
    [props.isAuthenticated, navigate],
  );

  const AccountDropdown = ({ prefix }: { prefix: string }) => {
    const id = `${prefix}-account`;
    const isOpen = activeDropdownId === id;
    return (
      <div className={`cy-dropdown ${isOpen ? 'open' : ''}`}>
        <button
          className="nav-btn"
          onClick={e => {
            e.stopPropagation();
            toggleDropdown(id);
          }}
        >
          <FontAwesomeIcon icon="user" className="nav-icon" />
          Account
          <FontAwesomeIcon icon="chevron-right" className="dropdown-arrow" />
        </button>
        <div className="cy-dropdown-menu">
          {props.isAuthenticated ? (
            <>
              <Link to="/account/settings" className="dropdown-link" onClick={() => setActiveDropdownId(null)}>
                Settings
              </Link>
              <Link to="/account/password" className="dropdown-link" onClick={() => setActiveDropdownId(null)}>
                Password
              </Link>
              <Link to="/logout" className="dropdown-link" onClick={() => setActiveDropdownId(null)}>
                Sign out
              </Link>
            </>
          ) : (
            <>
              <Link to="/login" className="dropdown-link" onClick={() => setActiveDropdownId(null)}>
                Sign In
              </Link>
              <Link to="/account/register" className="dropdown-link" onClick={() => setActiveDropdownId(null)}>
                Register
              </Link>
            </>
          )}
        </div>
      </div>
    );
  };

  const EntitiesDropdown = ({ prefix }: { prefix: string }) => {
    const id = `${prefix}-entities`;
    const isOpen = activeDropdownId === id;
    return (
      <div className={`cy-dropdown ${isOpen ? 'open' : ''}`}>
        <button
          className="nav-btn"
          onClick={e => {
            e.stopPropagation();
            toggleDropdown(id);
          }}
        >
          <FontAwesomeIcon icon="th-list" className="nav-icon" />
          Entities
          <FontAwesomeIcon icon="chevron-right" className="dropdown-arrow" />
        </button>
        <div className="cy-dropdown-menu">
          <Link to="/job-request" className="dropdown-link" onClick={() => setActiveDropdownId(null)}>
            Job Request
          </Link>
          <Link to="/sanitization-report" className="dropdown-link" onClick={() => setActiveDropdownId(null)}>
            Sanitization Report
          </Link>
          <Link to="/job-execution-report" className="dropdown-link" onClick={() => setActiveDropdownId(null)}>
            Job Execution Report
          </Link>
          <Link to="/job-report" className="dropdown-link" onClick={() => setActiveDropdownId(null)}>
            Job Report
          </Link>
        </div>
      </div>
    );
  };

  const AdminDropdown = ({ prefix }: { prefix: string }) => {
    const id = `${prefix}-admin`;
    const isOpen = activeDropdownId === id;
    return (
      <div className={`cy-dropdown ${isOpen ? 'open' : ''}`}>
        <button
          className="nav-btn"
          onClick={e => {
            e.stopPropagation();
            toggleDropdown(id);
          }}
        >
          <FontAwesomeIcon icon="users-cog" className="nav-icon" />
          Admin
          <FontAwesomeIcon icon="chevron-right" className="dropdown-arrow" />
        </button>
        <div className="cy-dropdown-menu">
          <Link to="/admin/user-management" className="dropdown-link" onClick={() => setActiveDropdownId(null)}>
            User Management
          </Link>
          <Link to="/admin/metrics" className="dropdown-link" onClick={() => setActiveDropdownId(null)}>
            Metrics
          </Link>
          <Link to="/admin/health" className="dropdown-link" onClick={() => setActiveDropdownId(null)}>
            Health
          </Link>
          <Link to="/admin/configuration" className="dropdown-link" onClick={() => setActiveDropdownId(null)}>
            Configuration
          </Link>
          <Link to="/admin/logs" className="dropdown-link" onClick={() => setActiveDropdownId(null)}>
            Logs
          </Link>
          {props.isOpenAPIEnabled && (
            <Link to="/admin/docs" className="dropdown-link" onClick={() => setActiveDropdownId(null)}>
              API Docs
            </Link>
          )}
        </div>
      </div>
    );
  };

  return (
    <div id="app-header">
      <LoadingBar className="loading-bar" />

      <header className="topbar" data-cy="navbar">
        <Link to="/" className="topbar-brand">
          <img src="content/images/it_logo.svg" alt="Logo" className="topbar-logo" />
          <span className="topbar-title">CyberAI</span>
        </Link>

        <nav className={`header-nav ${hasScrolledPastBanner ? 'is-visible' : 'is-hidden'}`}>
          <button onClick={() => setDarkMode(prev => !prev)} className="nav-btn" title={darkMode ? 'Light' : 'Dark'}>
            <FontAwesomeIcon icon={darkMode ? 'sun' : 'moon'} className="nav-icon" />
            {darkMode ? 'Light' : 'Dark'}
          </button>
          <a href="/" onClick={goHome} className="nav-btn">
            <FontAwesomeIcon icon="home" className="nav-icon" /> Home
          </a>
          {props.isAuthenticated && <EntitiesDropdown prefix="header" />}
          {props.isAuthenticated && props.isAdmin && <AdminDropdown prefix="header" />}
          <AccountDropdown prefix="header" />
        </nav>
      </header>

      <aside className={`sidebar-panel ${hasScrolledPastBanner ? 'is-hidden' : ''}`}>
        <div className="sidebar-section">
          <div className="sidebar-label">Navigation</div>
          <ul className="sidebar-list">
            <li>
              <a href="/" onClick={goHome} className="nav-btn active">
                <FontAwesomeIcon icon="home" className="nav-icon" /> Home
              </a>
            </li>
            {props.isAuthenticated && (
              <li>
                <EntitiesDropdown prefix="sidebar" />
              </li>
            )}
            {props.isAuthenticated && props.isAdmin && (
              <li>
                <AdminDropdown prefix="sidebar" />
              </li>
            )}
            <li>
              <AccountDropdown prefix="sidebar" />
            </li>
            <li>
              <button onClick={() => setDarkMode(prev => !prev)} className="nav-btn" title={darkMode ? 'Light mode' : 'Dark mode'}>
                <FontAwesomeIcon icon={darkMode ? 'sun' : 'moon'} className="nav-icon" />
                {darkMode ? 'Light Mode' : 'Dark Mode'}
              </button>
            </li>
          </ul>
        </div>
      </aside>
    </div>
  );
};

export default Header;

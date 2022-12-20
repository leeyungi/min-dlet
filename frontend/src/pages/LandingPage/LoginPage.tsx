import React, { useEffect ,useState } from 'react';
import classNames from 'classnames/bind';
import styles from './LandingPage.module.scss';
import Background from 'components/Landing/Background'
import Login from 'components/Landing/Login'

// const cx = classNames.bind(styles);
const LoginPage = () => {


  return (
    <section style={{
      width: "100%", 
      height: "100vh", 
      overflow: "hidden",
      display: "table"}}>
      {/* <h1>제발!!</h1> */}
      <Login / >
      <Background />
    </section>
  );
};

export default LoginPage;
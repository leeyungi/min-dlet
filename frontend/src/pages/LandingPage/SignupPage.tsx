import React from 'react';
import Background from 'components/Landing/Background';
import Signup from 'components/Landing/Signup';

// const cx = classNames.bind(styles);
const SignupPage = () => {
  return (
    <section
      style={{
        width: '100%',
        height: '100vh',
        overflow: 'hidden',
      }}
    >
      {/* <h1>제발!!</h1> */}
      <Signup />
      <Background />
    </section>
  );
};

export default SignupPage;

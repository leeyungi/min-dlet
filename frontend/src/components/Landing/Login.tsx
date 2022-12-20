import React, { useRef, useEffect } from 'react';
import classNames from 'classnames/bind';
import styles from 'pages/LandingPage/LandingPage.module.scss';
import { useNavigate } from 'react-router';
import toast from 'react-hot-toast';
import { useRecoilState } from 'recoil';
import memberState from 'utils/memberState';
import title from 'assets/images/title.png';
const BaseURL = process.env.REACT_APP_BASE_URL;

const cx = classNames.bind(styles);
const Login = () => {
  const [, setMember] = useRecoilState(memberState);
  const idInput = useRef<HTMLInputElement>(document.createElement('input'));
  const passwordInput = useRef<HTMLInputElement>(
    document.createElement('input')
  );
  const navigate = useNavigate();

  useEffect(() => {
    if (localStorage.getItem('token')) {
      navigate('/');
    }
  });

  // 로그인 함수
  const handleLoginClick = async () => {
    const loginData = {
      method: 'POST',
      body: JSON.stringify({
        id: idInput.current.value,
        password: passwordInput.current.value,
      }),
      headers: {
        'Content-Type': 'application/json',
      },
    };

    try {
      const res = await fetch(`${BaseURL}members/login`, loginData);
      const result = await res.json();
      const data = result.data;
      if (res.status === 200) {
        localStorage.setItem('token', data.jwtToken);
        delete data.jwtToken;
        setMember(data);
        navigate('/');
      } else {
        toast.error(`아이디가 존재하지 않거나 
        잘못된 비밀번호입니다😥`);
      }
    } catch {
      toast.error(`아이디가 존재하지 않거나 
        잘못된 비밀번호입니다😥`);
    }
  };

  return (
    <div className={cx('member-modal')}>
      <div className={cx('title')}>
        <img src={title} alt="title"></img>
      </div>
      <form>
        <div>
          <h3>아이디</h3>
          <input
            id="idinput"
            maxLength={12}
            ref={idInput}
            type="text"
            placeholder="ID"
          ></input>
        </div>
        <div className={cx('id-validation-check')}></div>
        <div>
          <h3>비밀번호</h3>
          <input
            ref={passwordInput}
            id="passwordinput"
            placeholder="Password"
            type="password"
            maxLength={20}
            onKeyDown={(e) => {
              if (e.key === 'Enter') {
                handleLoginClick();
              }
            }}
          ></input>
        </div>
        <button type="button" onClick={handleLoginClick}>
          로그인
        </button>
      </form>
      <button
        type="button"
        onClick={() => {
          navigate('/signup');
        }}
      >
        회원이 아니신가요?
      </button>
    </div>
  );
};

export default Login;

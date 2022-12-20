import React, { useState, useRef } from 'react';
import classNames from 'classnames/bind';
import styles from 'pages/LandingPage/LandingPage.module.scss';
import { useNavigate } from 'react-router';
import title from 'assets/images/title.png';
import toast from 'react-hot-toast';

const BaseURL = process.env.REACT_APP_BASE_URL;

const cx = classNames.bind(styles);
const Login = () => {
  const [isShow, setIsShow] = useState(false);
  const [isValidId, setIsValidId] = useState(false);
  // const [isValidPassword, setIsValidPassword] = useState(true)
  const [isValidConfirm, setIsValidConfirm] = useState(true);
  const idInput = useRef<HTMLInputElement>(document.createElement('input'));
  const passwordInput = useRef<HTMLInputElement>(
    document.createElement('input')
  );
  const passwordConfirmInput = useRef<HTMLInputElement>(
    document.createElement('input')
  );
  const navigate = useNavigate();

  // 아이디 중복체크 함수
  const handleValidationClick = async () => {
    // const idInput = document.getElementById('idinput')
    if (idInput.current.value.length < 5) {
      return;
    }
    try {
      const result = await fetch(
        `${BaseURL}members/id-duplicate-check/${idInput.current.value}`
      );
      if (result.status === 204) {
        setIsShow(true);
        setIsValidId(true);
      } else {
        setIsShow(true);
        setIsValidId(false);
      }
    } catch (err) {
      setIsShow(true);
      setIsValidId(false);
    }
  };

  const movePageLogin = () => {
    navigate('/login');
  };

  // 로그인 함수
  const handleSignupClick = async () => {
    if (!(isShow && isValidId)) {
      toast.error('아이디 중복확인을 진행해 주세요.');
      return;
    }

    if (passwordInput.current.value !== passwordConfirmInput.current.value) {
      return;
    }

    if (passwordInput.current.value.includes(idInput.current.value)) {
      toast.error('비밀번호에 아이디가 포함되어 있어요.');
      return;
    }

    if (
      passwordInput.current.value.length < 8 ||
      !/[a-z]/.test(passwordInput.current.value) ||
      !/[0-9]/.test(passwordInput.current.value) ||
      !/[\[\]\{\}\/\(\)\.\?\<\>!@#$%^&*]/.test(passwordInput.current.value)
    ) {
      toast.error('비밀번호 조건을 다시 확인해 주세요.');
      return;
    }

    const signupData = {
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
      const res = await fetch(`${BaseURL}members`, signupData);
      // const data = await res.json()
      if (res.status === 201) {
        toast.success(`mindlet에 오신 걸 축하합니다!
        로그인 페이지로 이동합니다.`);
        setTimeout(() => {
          navigate('/login');
        }, 1000);
        return;
      } else {
        toast.error(`😥회원가입이 정상적으로 이루어지지 않았습니다😥`);
      }
    } catch {
      toast.error(`😥회원가입이 정상적으로 이루어지지 않았습니다😥`);
    }
  };

  const pwConfirm = (e: React.ChangeEvent<HTMLInputElement>) => {
    if (e.target.value.length >= 8) {
      if (e.target.value !== passwordInput.current.value) {
        setIsValidConfirm(false);
      } else {
        setIsValidConfirm(true);
      }
    }
  };

  return (
    <section className={cx('member-modal')}>
      {/* <Toaster /> */}
      <div className={cx('title')}>
        <img src={title} alt="title"></img>
      </div>
      <form style={{ marginTop: '10px', lineHeight: '120%' }}>
        <div>
          <h3>아이디</h3>
          <div style={{ width: '108%', lineHeight: '120%' }}>
            <span style={{ fontSize: '0.8em' }}>
              5~20자의 영문 소문자, 숫자만 사용 가능
            </span>
          </div>
          <input
            id="idinput"
            maxLength={12}
            ref={idInput}
            type="text"
            placeholder="영문 소문자, 숫자"
            onChange={() => {
              setIsShow(false);
            }}
          ></input>
        </div>
        <div className={cx('id-validation-check')}>
          <div>
            {isShow &&
              (isValidId ? (
                <span
                  style={{
                    textAlign: 'left',
                    color: 'green',
                    fontSize: '12px',
                  }}
                >
                  사용 가능한 ID 입니다
                </span>
              ) : (
                <span
                  style={{
                    textAlign: 'left',
                    color: 'red',
                    fontSize: '12px',
                    lineHeight: '100%',
                  }}
                >
                  사용할 수 없는 ID 입니다.
                </span>
              ))}
          </div>
          <div>
            <button
              type="button"
              onClick={handleValidationClick}
              style={{
                marginTop: 0,
                marginLeft: 'auto',
                background: 'none',
                color: 'black',
                textAlign: 'center',
                width: '80px',
              }}
            >
              중복확인
            </button>
          </div>
        </div>
        <div>
          <h3>비밀번호</h3>
          <div style={{ width: '108%', lineHeight: '120%' }}>
            <span style={{ fontSize: '0.8em' }}>
              8~20자의 영문 소문자, 숫자, 특수문자
            </span>
          </div>
          <input
            ref={passwordInput}
            id="passwordinput"
            type="password"
            maxLength={20}
          ></input>
        </div>
        <div style={{ marginTop: '10px' }}>
          <h3>비밀번호 확인</h3>
          <input
            ref={passwordConfirmInput}
            id="passwordconfirminput"
            type="password"
            maxLength={20}
            onChange={pwConfirm}
            onKeyDown={(e) => {
              if (e.key === 'Enter') {
                handleSignupClick();
              }
            }}
          ></input>
        </div>
        <div>
          {isValidConfirm ? null : (
            <span style={{ color: 'red' }}>
              입력하신 비밀번호와 확인이 다릅니다.
            </span>
          )}
        </div>
        <button type="button" onClick={handleSignupClick}>
          회원가입
        </button>
        <div
          style={{
            textAlign: 'center',
            marginTop: '20px',
            marginBottom: '30px',
            color: 'black',
            fontSize: '0.8em',
          }}
          onClick={movePageLogin}
        >
          로그인 하러 가기
        </div>
      </form>
    </section>
  );
};

export default Login;

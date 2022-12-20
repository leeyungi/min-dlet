import React, { useEffect, useState } from 'react';
import { useNavigate } from 'react-router';
import { sound } from 'utils/soundRecognize';
import BlowAnimation from 'components/Animation/BlowAnimation/BlowAnimation';
import classNames from 'classnames/bind';
import { postContents } from 'services/api/Contents';
import toast, { Toaster } from 'react-hot-toast';
import styles from './Blow.module.scss';
const cx = classNames.bind(styles);
const Blow = ({ onClick, form, setForm }: any) => {
  const [isShowing, setIsShowing] = useState(true);
  const [sendSuccess, SetsendSuccess] = useState(false);
  const [loading, SetLoading] = useState(false);
  const [endstate, SetEndstate] = useState(false);
  const [touchstate, SetTouchstate] = useState(false);
  const [throttle, setThrottle] = useState(false);
  const [checkState, SetCheckState] = useState(0);
  const [possibleState, SetState] = useState(false);
  const [wind, SetWind] = useState(false);
  const navigate = useNavigate();
  const blow = () => {
    SetLoading(true);
    window.removeEventListener('blow', blow);
    SetWind(true);
  };

  const skipBtn = async () => {
    if (loading === false) {
      //api 전송 전
      const result = await sendDataForm();
      console.log(result);
      if (result === true) {
        toast('전송에 성공하였습니다.', {
          icon: '🌼',
          style: {
            borderRadius: '10px',
            background: '#333',
            color: '#fff',
          },
        });
      } else {
        toast('전송에 실패하였습니다.', {
          icon: '🌼',
          style: {
            borderRadius: '10px',
            background: '#333',
            color: '#fff',
          },
        });
      }
    } else if (loading === true && sendSuccess === true) {
      //api 전송 성공 후
      toast('전송에 성공하였습니다.', {
        icon: '🌼',
        style: {
          borderRadius: '10px',
          background: '#333',
          color: '#fff',
        },
      });
    } else if (loading === true && sendSuccess === false) {
      //api 전송 실패
      toast('전송에 실패하였습니다.', {
        icon: '🌼',
        style: {
          borderRadius: '10px',
          background: '#333',
          color: '#fff',
        },
      });
    }
    //api 전송 전
    navigate('/');
  };

  const sendDataForm = async () => {
    console.log(form.date);
    const formData = new FormData();
    formData.append('imageFile', form.image);
    formData.append(
      'dandelionRegisterForm',
      new Blob(
        [
          JSON.stringify({
            message: form.message,
            blossomedDate: form.date,
          }),
        ],
        {
          type: 'application/json',
        }
      )
    );
    try {
      setThrottle(true);
      const response = await postContents(formData);
      if (response.status === 201) {
        setThrottle(false);
        SetsendSuccess(true);
        return true;
      } else {
        console.log(response);
        setThrottle(false);
        return false;
      }
    } catch (error) {
      toast('전송에 실패하였습니다.', {
        icon: '🌼',
        style: {
          borderRadius: '10px',
          background: '#333',
          color: '#fff',
        },
      });
      navigate('/');
      setThrottle(false);
      return false;
    }
  };

  useEffect(() => {
    // console.log(endstate);
    if (endstate === true) {
      toast('전송에 성공하였습니다.', {
        icon: '🌼',
        style: {
          borderRadius: '10px',
          background: '#333',
          color: '#fff',
        },
      });
      navigate('/');
    }
  }, [endstate]);

  useEffect(() => {
    // console.log(form);
    if (loading === true) {
      handleSend();
      console.log('전송');
    }
  }, [loading]);

  const stateDetect = (state: boolean) => {
    SetEndstate(state);
  };
  const msgDetect = (state: number) => {
    SetCheckState(state);
  };

  const possible = (state: boolean) => {
    SetState(state);
  };

  const touchDetect = (state: boolean) => {
    SetTouchstate(true);
  };

  const handleSend = () => {
    if (throttle) return;
    if (!throttle) {
      sendDataForm();
    }
  };
  // 초기값 0 가능하면 1 불가능하면 2

  useEffect(() => {
    if (possibleState) {
      sound();
      window.addEventListener('blow', blow);
      return () => {
        window.removeEventListener('blow', blow);
      };
    }
  }, [possibleState]);
  return (
    <div
      style={{
        width: '100%',
        height: '100vh',
        overflow: 'hidden',
      }}
    >
      <button className={cx('skip')} onClick={skipBtn}>
        Skip
      </button>
      <Toaster position="top-center" reverseOrder={false} />
      {isShowing && (
        <>
          {possibleState && !wind && (
            <div className={cx('windGuide')}>바람을 불어주세요</div>
          )}
          <BlowAnimation
            endstate={stateDetect}
            msgCheck={msgDetect}
            isPossible={possible}
            windState={wind}
            touchEvt={touchDetect}
          />
        </>
      )}
    </div>
  );
};

export default Blow;

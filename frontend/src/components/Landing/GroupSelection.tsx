import React, { useState } from 'react';
import classNames from 'classnames/bind';
import styles from 'pages/LandingPage/LandingPage.module.scss';
import { useRecoilState } from 'recoil';
import memberState from 'utils/memberState';
import { toast } from 'react-hot-toast';
import { useNavigate } from 'react-router';

const BaseURL = process.env.REACT_APP_BASE_URL;
const cx = classNames.bind(styles);

function GroupSelection({
  setIsGroupShowing = () => {},
  setAudioNow,
  stop,
  audioNow,
}: React.SetStateAction<React.ComponentState>) {
  const [communities] = useState(['KOREA', 'WORLD', 'US']);
  const [member, setMember] = useRecoilState(memberState);
  const navigate = useNavigate();

  const handleIsGroupShowing = () => {
    setIsGroupShowing(false);
  };

  const setCommunity = (comm: string) => {
    const new_member = { ...member };
    new_member.community = comm;
    setMember(new_member);
  };

  const handleCommunity = async (e: any) => {
    if (communities[e.target.selectedIndex] === member.community) {
      return;
    } else {
      const checkData = {
        method: 'PATCH',
        headers: {
          Authorization: `Bearer ${localStorage.getItem('token')}`,
          'Content-Type': 'application/json',
        },

        body: JSON.stringify({
          community: communities[e.target.selectedIndex],
        }),
      };
      // const res = await fetch(`${BaseURL}members/${member.seq}/community`, checkData)
      try {
        const res = await fetch(
          `${BaseURL}members/${member.seq}/community`,
          checkData
        );
        if (res.status === 200) {
          // const result = await res.json()
          setCommunity(communities[e.target.selectedIndex]);
        } else {
          toast.error('채널 변경에 실패하였습니다');
        }
      } catch {
        toast.error('채널 변경에 실패하였습니다');
      }
    }
  };

  return (
    <div
      style={{
        top: 0,
        zIndex: 1,
        left: 0,
        width: '100%',
        height: '100%',
        backgroundColor: 'rgba(0, 0, 0, 0.2)',
        position: 'absolute',
      }}
    >
      <div
        style={{
          zIndex: 3,
          position: 'absolute',
          top: '35%',
          left: '50%',
          width: 'min(90vw, 400px)',
          height: 'min(30vh, 300px)',
          padding: '40px',
          textAlign: 'center',
          backgroundColor: '#FBFBFB66',
          borderRadius: '10px',
          boxShadow: '0 2px 3px 0 rgba(34, 36, 38, 0.15)',
          transform: 'translateX(-50%) translateY(-50%)',
        }}
      >
        <h1 style={{ color: 'white', fontWeight: 'normal' }}>채널 설정</h1>
        <hr />
        {/* <label htmlFor="options">허허</label> */}
        <select
          defaultValue={member.community}
          onChange={handleCommunity}
          style={{ marginTop: '10px', width: '70%', fontSize: '16px' }}
          name="score"
          id="options"
        >
          {communities.map((value, idx) => {
            return (
              <option value={value} key={idx}>
                {value}
              </option>
            );
          })}
        </select>
        <div
          style={{
            textAlign: 'end',
            position: 'absolute',
            bottom: '20px',
            right: '20px',
          }}
        >
          {/* <button
            style={{
              fontSize: '20px',
              borderRadius: '10%',
              border: '2px solid #568BDA',
              padding: '5px',
            }}
            onClick={(e) => {
              e.stopPropagation();
              const newAudio = { ...audioNow };
              newAudio.landing = false;
              setAudioNow(newAudio);
              stop();
              navigate('/guide');
            }}
          >
            가이드 보기
          </button> */}
        </div>
      </div>
      <div
        onClick={handleIsGroupShowing}
        style={{ width: '100%', height: '100%', position: 'relative' }}
      ></div>
    </div>
  );
}

export default GroupSelection;

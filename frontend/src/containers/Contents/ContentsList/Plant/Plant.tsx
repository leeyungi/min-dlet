import React, { useEffect, useState } from 'react';
import classNames from 'classnames/bind';
import styles from './Plant.module.scss';
import toast, { Toaster } from 'react-hot-toast';
import { useNavigate } from 'react-router';
import FlyingAnimation from 'components/Animation/PlantAnimation/FlyingAnimation';
import { getContents } from 'services/api/Contents';
const cx = classNames.bind(styles);
const Plant = ({ onClick, seq, count, list }: any) => {
  const navigate = useNavigate();
  const [endstate, SetEndstate] = useState(false);
  const [checkState, SetCheckState] = useState(0);
  const [possibleState, SetState] = useState(0);

  const stateDetect = (state: boolean) => {
    SetEndstate(state);
  };

  const msgDetect = (state: number) => {
    SetCheckState(state);
  };

  const possible = (state: number) => {
    SetState(state);
  };

  const skipBtn = () => {
    onClick(1);
  };

  const [isShowing, setIsShowing] = useState(true);

  useEffect(() => {
    //   console.log(endstate);
    if (endstate === true) {
      onClick(1);
    }
  }, [endstate]);

  useEffect(() => {
    //    console.log(endstate);
  }, [possibleState]);

  useEffect(() => {
    //   console.log(checkState);
    possible(1);
    //화면을 누르면  api 호출해서 가능한지 확인
    //가능하면 넌겨주고 안되면 toast 후 home으로
  }, [checkState]);

  return (
    <div
      style={{
        width: '100%',
        height: '100vh',
        overflow: 'hidden',
      }}
    >
      {isShowing && (
        <>
          <button className={cx('skip')} onClick={skipBtn}>
            Skip
          </button>
          <FlyingAnimation
            endstate={stateDetect}
            msgCheck={msgDetect}
            isPossible={possible}
          ></FlyingAnimation>
        </>
      )}
      <Toaster />
    </div>
  );
};

export default Plant;

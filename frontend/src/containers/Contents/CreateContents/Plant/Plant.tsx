import React, { useEffect, useState } from 'react';
import classNames from 'classnames/bind';
import styles from './Plant.module.scss';
import { useNavigate } from 'react-router';
import LandingModel from 'components/Landing/LandingModel';
import { ReactComponent as Menu } from 'assets/images/menu.svg';
import NewPlantAnimation from 'components/Animation/PlantAnimation/NewPlantAnimation';

const cx = classNames.bind(styles);
const Plant = ({ onClick }: any) => {
  const [endstate, SetEndstate] = useState(false);

  let [xStart, yStart, xEnd, yEnd] = [0, 0, 0, 0];
  let howManyTouches = 0;

  const stateDetect = (state: boolean) => {
    SetEndstate(state);
  };

  const skipBtn = () => {
    onClick(1);
  };

  const [isShowing, setIsShowing] = useState(true);

  useEffect(() => {
    if (endstate === true) {
      onClick(1);
    }
  }, [endstate]);

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
          <NewPlantAnimation endstate={stateDetect} />
        </>
      )}
    </div>
  );
};

export default Plant;

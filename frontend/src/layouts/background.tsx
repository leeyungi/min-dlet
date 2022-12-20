import React from 'react';
import classNames from 'classnames/bind';
import { Outlet } from 'react-router-dom';
import styles from './background.module.scss';
import cloud1 from 'assets/images/cloud_img1.png';
import cloud2 from 'assets/images/cloud_img2.png';
import cloud3 from 'assets/images/cloud_img3.png';
const cx = classNames.bind(styles);
const background = () => {
  return (
    <div className={cx('data')}>
      <div className={cx('cloud1')}>
        <img src={cloud1} alt="cloud1" />
      </div>
      <div className={cx('cloud2')}>
        <img src={cloud2} alt="cloud2" />
      </div>
      <div className={cx('cloud3')}>
        <img src={cloud3} alt="cloud3" />
      </div>
      <Outlet />
    </div>
  );
};

export default background;

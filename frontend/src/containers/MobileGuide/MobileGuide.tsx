import React from 'react';
import classNames from 'classnames/bind';
import styles from './MobileGuide.module.scss';
import LandingModel from 'components/Landing/LandingModel';
import cloud1 from 'assets/images/cloud_img1.png';
import cloud2 from 'assets/images/cloud_img2.png';
import cloud3 from 'assets/images/cloud_img3.png';

const cx = classNames.bind(styles);

const MobileGuide = () => {
  return (
    <section className={cx('container')}>
      <div className={cx('cloud1')}>
        <img className={cx('cloud1-img')} src={cloud1} alt="cloud1" />
      </div>
      <div className={cx('cloud2')}>
        <img className={cx('cloud2-img')} src={cloud2} alt="cloud2" />
      </div>
      <div className={cx('cloud3')}>
        <img className={cx('cloud3-img')} src={cloud3} alt="cloud3" />
      </div>
      <div className={cx('cloud4')}>
        <img className={cx('cloud4-img')} src={cloud3} alt="cloud3" />
      </div>
      <div className={cx('cloud5')}>
        <img className={cx('cloud5-img')} src={cloud2} alt="cloud2" />
      </div>
      <div className={cx('title')}>모바일 환경에서 접속해주세요</div>
      <LandingModel />
    </section>
  );
};

export default MobileGuide;

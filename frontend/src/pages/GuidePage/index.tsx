import React, { useEffect } from 'react';
import styles from "./GuidePage.module.scss";
import classNames from "classnames/bind";
import landing from "assets/images/Guide/랜딩페이지.png";
import contentCreate from "assets/images/Guide/민들레씨작성.png";
import blow from "assets/images/Guide/민들레불기애니.png";
import flowerGarden from "assets/images/Guide/꽃밭.png";
import album from "assets/images/Guide/보관함.png";
import setting from "assets/images/Guide/설정.png";
import { useNavigate } from 'react-router';

const cx = classNames.bind(styles);

function GuidePage () {
  const navigate = useNavigate()
  // document.body.style.height = "auto"
  return (
  <section className={cx('guides')}> 
    <section className={cx('guide')}>
      <img className={cx('guide__image')} src={landing} alt="랜딩페이지 설명"></img>
      <div style={{top: "45%"}} className={cx('start')}>
        <button
        className={cx('guide-button')}
        onClick ={() => {
          navigate('/')
        }}
        >
          시작하기
        </button>
      </div>
    </section>
    <section className={cx('guide')}>
      <img src={contentCreate} alt="랜딩페이지 설명"></img>
    </section>
    <section className={cx('guide')}>
      <img src={blow} alt="민들레 씨 불기"></img>
    </section>
    <section className={cx('guide')}>
      <img src={flowerGarden} alt="꽃밭"></img>
    </section>
    <section className={cx('guide')}>
      <img src={album} alt="보관함"></img>
    </section>
    <section className={cx('guide')}>
      <img src={setting} alt="설정창"></img>
      <div className={cx('start')}>
        <button 
        className={cx('guide-button')}
        onClick ={() => {
          navigate('/')
        }}
        >
          시작하기
        </button>
      </div>
    </section>
  </section>

  
  
)};

export default GuidePage;

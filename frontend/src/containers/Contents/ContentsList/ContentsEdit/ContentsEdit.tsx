import ContentsEditor from 'components/ContentsEditor/ContentsEditor';
import React, { useEffect, useState } from 'react';
import classNames from 'classnames/bind';
import styles from './ContentsEdit.module.scss';
import iconimg from 'assets/images/icon/earth-globe-white.png';
import backimg from 'assets/images/icon/left-arrow.png';
import { useNavigate } from 'react-router-dom';
const cx = classNames.bind(styles);

const ContentsEdit = ({ onClick, form, setForm }: any) => {
  const [msg, SetMsg] = useState();
  const [img, SetImg] = useState();
  const navigate = useNavigate();
  useEffect(() => {
    SetMsg(form.message);
    SetImg(form.image);
  }, []);

  const check = () => {
    setForm({ ...form, image: img, message: msg });
    onClick(3);
  };

  const back = () => {
    setForm({ ...form, image: img, message: msg });
    onClick(1);
  };

  const home = () => {
    navigate('/');
  };

  return (
    <>
      <div className={cx('container')}>
        <img
          className={cx('home-btn')}
          src={iconimg}
          onClick={home}
          alt="home"
        />
      </div>
      <ContentsEditor form={form} img={SetImg} msg={SetMsg} onSend={check} />
      <button onClick={back} className={cx('back-btn')}>
        <img className={cx('back-img')} src={backimg} alt="backbtn" />
      </button>
    </>
  );
};

export default ContentsEdit;

import ContentsEditor from 'components/ContentsEditor/ContentsEditor';
import React, { useEffect, useState } from 'react';
import classNames from 'classnames/bind';
import styles from './NewContentsEdit.module.scss';
import iconimg from 'assets/images/icon/earth-globe-white.png';
import { useNavigate } from 'react-router-dom';
const cx = classNames.bind(styles);

const NewContentsEdit = ({ onClick, form, setForm }: any) => {
  const [msg, SetMsg] = useState();
  const [img, SetImg] = useState();
  const navigate = useNavigate();
  useEffect(() => {
    SetMsg(form.message);
    SetImg(form.image);
  }, []);

  const check = () => {
    setForm({ ...form, image: img, message: msg });
    onClick(2);
  };

  const home = () => {
    navigate('/');
  };

  return (
    <>
      <div className={cx('')}>
        <img
          className={cx('home-btn')}
          src={iconimg}
          onClick={home}
          alt="home"
        />
      </div>
      <ContentsEditor form={form} img={SetImg} msg={SetMsg} onSend={check} />
    </>
  );
};

export default NewContentsEdit;

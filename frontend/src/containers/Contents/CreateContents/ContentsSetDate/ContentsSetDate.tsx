import React, { useEffect, useState } from 'react';
import classNames from 'classnames/bind';
import styles from './ContentsSetDate.module.scss';
import Calendar from 'react-calendar';
import backimg from 'assets/images/icon/left-arrow.png';
import { fDateDash } from 'utils/formatTime';
import 'react-calendar/dist/Calendar.css';
import moment from 'moment';
const cx = classNames.bind(styles);

const ContentsSetDate = ({ onClick, form, setForm }: any) => {
  const date: Date = new Date();
  date.setDate(date.getDate() + 2);
  const back = () => {
    onClick(2);
  };

  const setData = () => {
    //   console.log(form);
    onClick(4);
  };
  const [dateState, setDateState] = useState(date);
  const changeDate = (e: React.SetStateAction<Date>) => {
    setForm({ ...form, date: fDateDash(e.toString()) });
    setDateState(e);
  };

  useEffect(() => {
    setForm({ ...form, date: fDateDash(date.toString()) });
  }, []);

  return (
    <div className={cx('container')}>
      <div className={cx('calendar')}>
        <button className={cx('back-btn')} onClick={back}>
          <img className={cx('back-img')} src={backimg} alt="backbtn" />
        </button>
        <div className={cx('calendar-title')}>도착 할 날짜를 입력해주세요.</div>
        <Calendar
          className={cx('react-calendar')}
          formatDay={(locale, date) => moment(date).format('DD')}
          minDate={date}
          showNeighboringMonth={false}
          value={dateState}
          calendarType={"US"}
          onChange={changeDate}
        />
        {/* <div className={cx('selected-date')}>
          Selected: {moment(dateState).format('MMMM Do, YYYY')}
        </div> */}
        <div className={cx('btn')}>
          <button className={cx('send-btn')} onClick={setData}>
            Send
          </button>
        </div>
      </div>
    </div>
  );
};

export default ContentsSetDate;

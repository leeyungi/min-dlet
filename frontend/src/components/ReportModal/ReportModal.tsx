import React, { useEffect, useState } from 'react';
import classNames from 'classnames/bind';
import styles from './ReportModal.module.scss';
import toast, { Toaster } from 'react-hot-toast';
// import Select from 'react-select';
import { ReactComponent as Close } from 'assets/images/icon/cancel.svg';
import { reportContents } from 'services/api/Contents';
const cx = classNames.bind(styles);

const reportCategory = [
  { value: 'VIOLENCE', label: '폭력성' },
  { value: 'AD', label: '광고성' },
  { value: 'SEXUAL_HARASSMENT', label: '음란성' },
  { value: 'INAPPROPRIATE', label: '부적합' },
];

const ReportModal = ({ open, close, seq, complete, contents }: any) => {
  const [reason, setReason] = useState('VIOLENCE');
  const sendReport = async () => {
    const response = await reportContents({ seq, reason });
    close();
    toast.success('신고가 접수되었습니다.');
    complete(seq);
  };
  useEffect(() => {
    console.log(seq);
  }, [seq]);
  return (
    <div
      className={
        open ? [styles.openModal, styles.modal].join(' ') : styles['modal']
      }
    >
      {open ? (
        <section>
          <header>
            꽃잎 신고
            <button className={cx('close')} onClick={close}>
              <Close />
            </button>
          </header>
          <main>
            <div>" {contents} "</div>
            <div className={cx('title')}>
              해당 내용을
              <select onChange={(e) => setReason(e.target.value)} id="options">
                {reportCategory.map((value, idx) => {
                  return (
                    <option value={value.value} key={idx}>
                      {value.label}
                    </option>
                  );
                })}
              </select>
              의 사유로 신고합니다.
            </div>
          </main>
          <footer>
            <button onClick={sendReport}>신고하기</button>
          </footer>
        </section>
      ) : null}
    </div>
  );
};
export default ReportModal;

import sign from 'assets/images/signimg.png';
import classNames from 'classnames/bind';
import { useEffect } from 'react';
import styles from './MyGardenNoDandelion.module.scss';

const cx = classNames.bind(styles);

function MyGardenNoDandelion({ keynum }) {
  useEffect(() => {
    // console.log(keynum);
  }, []);
  return (
    <div className={cx('container')}>
      <div className={cx('icons')}></div>
      <div className={cx('sign-flower')}>
        <div className={cx('normal-sign')}>
          <img
            style={{ width: '100%' }}
            src={require(`assets/images/signimg${keynum}.png`)}
            alt="팻말"
          />
        </div>
      </div>
    </div>
  );
}
export default MyGardenNoDandelion;

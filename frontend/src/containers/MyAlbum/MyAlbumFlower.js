import styles from './MyAlbum.module.scss';
import classNames from 'classnames/bind';
import { useNavigate } from 'react-router-dom';

const cx = classNames.bind(styles);

function MyAlbumFlower({ dandelionSeq, description }) {
  const navigate = useNavigate();
  const Clickevt = () => {
    navigate(`/mygarden/dandelions/${dandelionSeq}`);
  };
  const modelNum = Math.floor(Math.random(dandelionSeq) * 4 + 1);
  return (
    <>
      <div className={cx('book__page__content__flower')} onClick={Clickevt}>
        <img
          style={{ maxHeight: '50%' }}
          alt="꽃"
          src={require(`assets/images/MyGarden/dandel${modelNum}.png`)}
        ></img>
        {/* <span>🌼꽃</span> */}
        <span>{description}</span>
        {/* <span>{dandelionSeq}</span> */}
      </div>
    </>
  );
}
export default MyAlbumFlower;

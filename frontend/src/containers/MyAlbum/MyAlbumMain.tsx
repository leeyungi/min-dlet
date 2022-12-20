import { useState, useEffect, useRef } from 'react';
import { useNavigate } from 'react-router-dom';
import styles from './MyAlbum.module.scss';
import classNames from 'classnames/bind';
import gear from 'assets/images/gear.png';
import garden from 'assets/images/garden.png';
import pencil from 'assets/images/pencil.png';
import MyAlbumSide from './MyAlbumSide';
import axios from 'axios';
import ButtonEffect from 'assets/musics/button_effect.wav';
import { useSound } from 'use-sound';
import { useRecoilValue } from 'recoil';
import memberState from 'utils/memberState';
// import Flip from 'assets/musics/page-flip-7.wav'
// import useSound from 'use-sound';

const cx = classNames.bind(styles);
const BaseURL = process.env.REACT_APP_BASE_URL;

const tempSide = [1, 1];

function MyAlbumMain() {
  const [sides, setSides] = useState([1]); // 앨범이 총 몇 페이지 있는지 저장
  const [totalPage, setTotalPage] = useState(0); // 앨범이 총 몇 페이지 있는지 저장
  // const [nowSide, setNowSide] = useState(1) // 현재 앨범 페이지
  const book = useRef<HTMLDivElement>(null);
  const member = useRecoilValue(memberState);
  // const [play, { stop, sound }] = useSound(Flip, {volume: 0.9, interrupt: true})
  const [play] = useSound(ButtonEffect, {
    volume: 0.4,
    interrupt: true,
  });

  const token = localStorage.getItem('token');
  const config = {
    Authorization: 'Bearer ' + token,
  };
  let xStart = 0;
  let xEnd = 0;
  let yStart = 0;
  let yEnd = 0;
  let howManyTouches = 0;

  const handleTouchStart = (e: TouchEvent) => {
    howManyTouches = e.touches.length;
    if (howManyTouches > 1) {
      return;
    }
    xStart = e.touches[0].clientX;
    yStart = e.touches[0].clientY;
  };

  const handleTouchEnd = (e: TouchEvent) => {
    if (howManyTouches > 1) {
      return;
    }
    xEnd = e.changedTouches[0].clientX;
    yEnd = e.changedTouches[0].clientY;
    if (Math.abs(yEnd - yStart) < 30) {
      if (xEnd - xStart > 50) {
        // console.log('swipe right');
        // console.log("여기까진1")
        if (tempSide[0] > 1) {
          // console.log("여기까진2")
          const inp = document.getElementById(`page-${tempSide[0] - 1}`);
          const target: HTMLInputElement = inp as HTMLInputElement;
          target.checked = false;
          tempSide[0] -= 1;
          // play()
        }
      } else if (xEnd - xStart < -50) {
        // console.log('swipe left');
        if (tempSide[0] < tempSide[1]) {
          const inp = document.getElementById(`page-${tempSide[0]}`);
          const target: HTMLInputElement = inp as HTMLInputElement;
          target.checked = true;
          tempSide[0] += 1;
          // play()
        }
      }
    }
  };

  // const add

  const customRange = (a: number) => {
    const arr = new Array(a).fill(1);
    const newArr = arr.map((value, idx) => idx + 1);
    return newArr;
  };

  useEffect(() => {
    const nowBook = book.current;
    nowBook?.addEventListener('touchstart', handleTouchStart);
    nowBook?.addEventListener('touchend', handleTouchEnd);
    axios({
      // url: `baseUrl/dandelions/{id}/description`, 나중에 아이디 있는거로 교체
      url: `garden/album?page=1&size=4`,
      method: 'GET',
      baseURL: BaseURL,
      headers: config,
    })
      .then((res) => {
        if (res.data.data.totalDandelionCount === 0) {
          tempSide[1] = 0;
          setSides([]);
          return;
        } else {
          tempSide[1] = res.data.data.totalPageNum;
          setTotalPage(res.data.data.totalPageNum)
          setSides(customRange(res.data.data.totalPageNum));
        }
      })
      .catch((err) => {
        setSides([]);
      });

    return () => {
      nowBook?.removeEventListener('touchstart', handleTouchStart);
      nowBook?.removeEventListener('touchend', handleTouchEnd);
    };
  }, []);

  // 안 되는 이유 확인,,,
  // const handleNowSide = (side: number, isRightFlip: boolean) => {
  //   if (isRightFlip) {
  //     if (side < sides.length) {
  //       const inp = document.getElementById(`page-${nowSide}`)
  //       const target: HTMLInputElement = inp as HTMLInputElement
  //       target.checked = true
  //       console.log('지금', nowSide)
  //       console.log(target)
  //       setNowSide((nowSide) => {return nowSide + 1})
  //       return
  //     } else {
  //       return
  //     }
  //   } else {
  //     if (side > 1) {
  //       setNowSide(side - 1)
  //     } else {
  //       return
  //     }
  //   }
  // }

  const navigate = useNavigate();

  const onSettingsClick = () => {
    navigate(`/settings`);
  };

  const onGardenClick = () => {
    navigate(`/mygarden`);
  };

  const onCabinetClick = () => {
    navigate(`/mygarden/cabinet`);
  };
  return (
    <div className={cx('container')}>
      <div>{/* <span>앨범</span> */}</div>
      <div className={cx('btns')}>
        <div>
          <button
            onClick={(e) => {
              if (!member.soundOff) {
                play();
              }
              onSettingsClick();
            }}
          >
            <img className={cx('btn')} src={gear} alt="설정" />
          </button>
        </div>
        <div>
          <button
            onClick={(e) => {
              if (!member.soundOff) {
                play();
              }
              onGardenClick();
            }}
          >
            <img className={cx('btn')} src={garden} alt="꽃밭" />
          </button>
        </div>
        {/* <div>
          <button onClick={(e) => {
            if (!member.soundOff) {
            play()}
            onCabinetClick()}}>
            <img className={cx("btn")} src={pencil} alt="기록보관함" />
          </button>
        </div> */}
      </div>

      {/* <div>
      </div> */}
      {/* <h1 style={{fontSize: "1.2rem", marginLeft: "16%"}}>내가 따로 저장한 꽃들</h1> */}
      <div ref={book} className={cx('cover')} style={{ position: 'relative' }}>
        <div className={cx('book')}>
          {/* <input style={{width: "20px", height: "20px", backgroundColor: "black", display: "none"}} type="radio" checked/> */}
          <div
            className={`${cx('book__page')} ${cx('book__page__first')}`}
          ></div>

          {sides.map((value: number) => {
            return <MyAlbumSide key={value} order={value} totalPage={sides.at(-1)} />;
          })}

          {/* <input style={{width: "20px", height: "20px", backgroundColor: "black"}} type="radio" value="good" name="page" id="page-2"/>
          <div style={{zIndex: 2}} className={cx('book__page')}>
            <div className={cx('book__page__content')}>
              jdasldjsaldjal
            </div>
          </div>

          <div className={cx('book__page')}>
            <span>내가 따로 저장한 꽃들</span>
            <div style={{display: "grid", gridTemplateColumns: "1fr 1fr"}}>
              <div>
                <span>꽃</span>
              </div>
              <div>
                <span>꽃</span>
              </div>
              <div>
                <span>헤이</span>
              </div>
              <div>
                <span>꽃</span>
              </div>
              <span style={{position: "absolute", bottom: "15px", right: "20px"}}>아ㅓㅁㄴ임나</span>
            </div>
          </div> */}

          <div className={cx('book__page')}>
            <h1
              style={{
                marginLeft: '20%',
                marginTop: '50%',
                fontSize: '1.2rem',
              }}
            >
              추억을 한 송이씩 채워보세요!
            </h1>
          </div>
        </div>
      </div>
    </div>
  );
}
export default MyAlbumMain;

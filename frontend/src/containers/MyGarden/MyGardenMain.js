import { useNavigate } from 'react-router-dom';
import gear from 'assets/images/gear.png';
import pencil from 'assets/images/pencil.png';
import album from 'assets/images/photo-album.png';
import earth2 from 'assets/images/earth2.png';
import styles from './MyGarden.module.scss';
import classNames from 'classnames/bind';
import { useEffect, useState } from 'react';
import MyGardenDandelion3 from './MyGardenDandelion3';
import MyGardenNoDandelion from './MyGardenNoDandelion';
import axios from 'axios';
import { motion } from 'framer-motion';
import { useSound } from 'use-sound';
import ButtonEffect from 'assets/musics/button_effect.wav';
import FlowerGarden from 'assets/musics/flower_garden.mp3';
import { useRecoilValue, useRecoilState } from 'recoil';
import memberState from 'utils/memberState';
import audioState from 'utils/audioState';

const cx = classNames.bind(styles);
const BaseURL = process.env.REACT_APP_BASE_URL;
const musicOn = [false];
const isMusicStopped = [false];

function MyGardenMain() {
  const member = useRecoilValue(memberState);
  const [audioNow, setAudioNow] = useRecoilState(audioState);
  const navigate = useNavigate();
  // const [play3, obj3] = useSound(FlowerGarden, {
  //   volume: 0.2,
  //   interrupt: true,
  // });
  // const sound3 = obj3.sound;
  // const [play2, obj2] = useSound(ButtonEffect, {
  //   volume: 0.4,
  //   interrupt: true,
  // });
  // const sound2 = obj2.sound;

  const [dandelions, setDandelions] = useState([]);
  const [dandelion0, setDandelion0] = useState(true);
  const [dandelion1, setDandelion1] = useState(true);
  const [dandelion2, setDandelion2] = useState(true);
  const [dandelion3, setDandelion3] = useState(true);
  const [dandelion4, setDandelion4] = useState(true);

  const onHomeClick = (e) => {
    // e.stopPropagation();
    // obj3.stop()
    // obj3.stop();
    // if (!member.soundOff) {
    //   play2();
    // }
    const newAudio = { ...audioNow };
    //  newAudio.flowerGarden = false;
    // setAudioNow(newAudio);
    navigate(`/`);
  };
  const onSettingsClick = () => {
    if (!member.soundOff) {
      // play2();
    }
    navigate(`/settings`);
  };

  const onAlbumClick = () => {
    if (!member.soundOff) {
      // play2();
    }
    navigate(`/mygarden/album`);
  };

  const onCabinetClick = () => {
    //play2();
    navigate(`/mygarden/cabinet`);
  };

  async function getGarden() {
    const token = localStorage.getItem('token');
    const config = {
      Authorization: 'Bearer ' + token,
    };
    await axios({
      url: `garden`,
      method: 'get',
      baseURL: BaseURL,
      headers: config,
    })
      .then((res) => {
        //   console.log('꽃밭 가져오기 성공');
        //  console.log(res.data.data);
        setDandelions(res.data.data);
        res.data.data.map((data, index) => {
          if (data == null && index === 0) {
            setDandelion0(false);
          } else if (data == null && index === 1) {
            setDandelion1(false);
          } else if (data == null && index === 2) {
            setDandelion2(false);
          } else if (data == null && index === 3) {
            setDandelion3(false);
          } else if (data == null && index === 4) {
            setDandelion4(false);
          }
        });
      })
      .catch((err) => {
        //   console.log('꽃밭 가져오기 실패');
        //   console.log(err);
      });
  }

  useEffect(() => {
    getGarden();
  }, []);

  return (
    <div
      className={cx('container')}
      // onClick={() => {
      //   if (!member.soundOff && !audioNow.flowerGarden) {
      //     if (sound3) {
      //       play3();
      //       const newAudio = { ...audioNow };
      //       newAudio.flowerGarden = true;
      //       setAudioNow(newAudio);
      //     }
      //   }
      // }}
    >
      <motion.div
        key={1}
        initial="hidden"
        animate="visible"
        exit={{ marginTop: '-300px', opacity: 0 }}
        variants={{
          hidden: {
            marginTop: '-300px',
            opacity: 0,
          },
          visible: {
            marginTop: 0,
            opacity: 1,
            transition: {
              delay: 0.5,
            },
          },
        }}
      >
        <div className={cx('btns')}>
          <div>
            <button type="button" onClick={onHomeClick}>
              <img className={cx('btn')} src={earth2} alt="랜딩페이지" />
            </button>
          </div>
          <div>
            <button type="button" onClick={onSettingsClick}>
              <img className={cx('btn')} src={gear} alt="설정" />
            </button>
          </div>
          <div>
            <button onClick={onAlbumClick}>
              <img className={cx('btn')} src={album} alt="앨범" />
            </button>
          </div>
          {/* <div>
            <button onClick={onCabinetClick}>
              <img className={cx("btn")} src={pencil} alt="기록보관함" />
            </button>
          </div> */}
        </div>

        <div className={cx('blank')}></div>

        <div className={cx('sign_boxs')}>
          {dandelion0 ? (
            <div className={cx('sign_box_5')}>
              {dandelions
                .slice(0, 1)
                .map((dandelion, index) =>
                  dandelion ? (
                    <MyGardenDandelion3
                      dandelion={dandelion}
                      setDandel={setDandelion0}
                      key={5}
                    />
                  ) : null
                )}
              {/* {[null, null, null, null, {seq: 11, 
          blossomedDate: new Date('2022-05-19'),
          status: "RETURN",
          }].slice(4, 5).map((dandelion, index) => (
            dandelion === null ? null:
            (<div key={index}>
              <MyGardenDandelion3 dandelion={dandelion} key={index} />
            </div>)
          ))} */}
            </div>
          ) : (
            <div className={cx('sign_box_5')}>
              <MyGardenNoDandelion keynum={5} />
            </div>
          )}

          {dandelion1 ? (
            <div className={cx('sign_box_4')}>
              {dandelions
                .slice(1, 2)
                .map((dandelion, index) =>
                  dandelion ? (
                    <MyGardenDandelion3
                      dandelion={dandelion}
                      setDandel={setDandelion1}
                      key={1}
                    />
                  ) : null
                )}
              {/* {[null, null, null, null, {seq: 11, 
          blossomedDate: new Date('2022-05-19'),
          status: "RETURN",
          }].slice(4, 5).map((dandelion, index) => (
            dandelion === null ? null:
            (<div key={index}>
              <MyGardenDandelion3 dandelion={dandelion} key={index} />
            </div>)
          ))} */}
            </div>
          ) : (
            <div className={cx('sign_box_4')}>
              <MyGardenNoDandelion keynum={4} />
            </div>
          )}

          {dandelion2 ? (
            <div className={cx('sign_box_3')}>
              {dandelions
                .slice(2, 3)
                .map((dandelion, index) =>
                  dandelion ? (
                    <MyGardenDandelion3
                      dandelion={dandelion}
                      setDandel={setDandelion2}
                      key={2}
                    />
                  ) : null
                )}
              {/* {[null, null, null, null, {seq: 11, 
          blossomedDate: new Date('2022-05-19'),
          status: "RETURN",
          }].slice(4, 5).map((dandelion, index) => (
            dandelion === null ? null:
            (<div key={index}>
              <MyGardenDandelion3 dandelion={dandelion} key={index} />
            </div>)
          ))} */}
            </div>
          ) : (
            <div className={cx('sign_box_3')}>
              <MyGardenNoDandelion keynum={3} />
            </div>
          )}

          {dandelion3 ? (
            <div className={cx('sign_box_2')}>
              {dandelions
                .slice(3, 4)
                .map((dandelion, index) =>
                  dandelion ? (
                    <MyGardenDandelion3
                      dandelion={dandelion}
                      setDandel={setDandelion3}
                      key={3}
                    />
                  ) : null
                )}
              {/* {[null, null, null, null, {seq: 11, 
          blossomedDate: new Date('2022-05-19'),
          status: "RETURN",
          }].slice(4, 5).map((dandelion, index) => (
            dandelion === null ? null:
            (<div key={index}>
              <MyGardenDandelion3 dandelion={dandelion} key={index} />
            </div>)
          ))} */}
            </div>
          ) : (
            <div className={cx('sign_box_2')}>
              <MyGardenNoDandelion keynum={2} />
            </div>
          )}

          {dandelion4 ? (
            <div className={cx('sign_box_1')}>
              {dandelions
                .slice(4, 5)
                .map((dandelion, index) =>
                  dandelion ? (
                    <MyGardenDandelion3
                      dandelion={dandelion}
                      setDandel={setDandelion4}
                      key={4}
                    />
                  ) : null
                )}
              {/* {[null, null, null, null, {seq: 11, 
          blossomedDate: new Date('2022-05-19'),
          status: "RETURN",
          }].slice(4, 5).map((dandelion, index) => (
            dandelion === null ? null:
            (<div key={index}>
              <MyGardenDandelion3 dandelion={dandelion} key={index} />
            </div>)
          ))} */}
            </div>
          ) : (
            <div className={cx('sign_box_1')}>
              <MyGardenNoDandelion keynum={1} />
            </div>
          )}
          {/* <div className={cx("sign_box_5")}>
          {[
            null,
            null,
            null,
            null,
            {
              seq: 11,
              blossomedDate: new Date("2022-05-19"),
              status: "RETURN",
            },
          ]
            .slice(4, 5)
            .map((dandelion, index) =>
              dandelion === null ? null : (
                <MyGardenDandelion3 dandelion={dandelion} key={index} />
              )
            )}
          {dandelions
            .slice(4, 5)
            .map((dandelion, index) =>
              dandelion ? (
                <MyGardenDandelion3 dandelion={dandelion} key={index} />
              ) : null
            )}
        </div> */}
        </div>
      </motion.div>
    </div>
  );
}
export default MyGardenMain;

import React, { useEffect, useState } from 'react';
import { useSprings, animated, to as interpolate } from '@react-spring/web';
import petal from 'assets/images/petal-yellow-4.png';
import classNames from 'classnames/bind';
import { useDrag } from '@use-gesture/react';
import iconimg from 'assets/images/icon/earth-globe-white.png';
import styles from './ContentsList.module.scss';
import { resetContentsState } from 'services/api/Contents';
import { useNavigate } from 'react-router-dom';
import { ReactComponent as SirenImg } from 'assets/images/icon/siren.svg';
import { petalCatchResultList, petalCatchResultSeq } from 'atoms/atoms';
import { useSetRecoilState } from 'recoil';
import ReportModal from 'components/ReportModal/ReportModal';
const cx = classNames.bind(styles);
// These two are just helpers, they curate spring data, values that are later being interpolated into css

const to = (i: number) => ({
  x: 0,
  y: i * -4,
  scale: 1,
  rot: -10 + Math.random() * 20,
  delay: i * 100,
});
const from = (_i: number) => ({ x: 0, rot: 0, scale: 1.5, y: -1000 });
// This is being used down there in the view, it interpolates rotation and scale into a css transform
const trans = (r: number, s: number) =>
  `perspective(1500px) rotateY(${r / 10}deg) rotateZ(${r}deg) scale(${s})`;

const ContentsList = ({ onClick, list, seq, count }: any) => {
  const navigate = useNavigate();
  const [reportlist, SetReportList] = useState([-1]);
  const [msg, SetMsg] = useState('');
  const [petalseq, SetSeq] = useState(0);
  const [modalOpen, setModalOpen] = useState(false);
  const setPetalData = useSetRecoilState(petalCatchResultList);
  const setPetalSeq = useSetRecoilState(petalCatchResultSeq);
  const [gone] = useState(() => new Set());
  const [props, api] = useSprings(count, (i) => ({
    ...to(i),
    from: from(i),
  }));

  const openModal = (i: number, msg: any) => {
    // console.log(i);
    console.log(msg);
    SetSeq(i);
    SetMsg(msg);
    setModalOpen(true);
  };
  const closeModal = () => {
    setModalOpen(false);
  };

  const home = async () => {
    const response = await resetContentsState(seq);
    setPetalData([{}]);
    setPetalSeq(0);
    navigate('/');
  };

  const send = () => {
    onClick(2);
  };

  const setBlind = (val: number) => {
    return reportlist.some(function (arrVal: number) {
      return val === arrVal;
    });
  };

  const reportComplete = () => {
    console.log('신고접수된 꽃잎', petalseq);
    SetReportList((prev) => [...prev, petalseq]);
  };

  useEffect(() => {
    console.log(list);
    console.log(seq);
    console.log(count);
  }, []);

  useEffect(() => {
    console.log('reportlist', reportlist);
  }, [reportlist]);

  const bind = useDrag(
    ({
      args: [index],
      active,
      movement: [mx],
      direction: [xDir],
      velocity: [vx],
    }) => {
      const trigger = vx > 0.2; // If you flick hard enough it should trigger the card to fly out
      if (!active && trigger) gone.add(index); // If button/finger's up and trigger velocity is reached, we flag the card ready to fly out
      api.start((i) => {
        if (index !== i) return; // We're only interested in changing spring-data for the current spring
        const isGone = gone.has(index);
        const x = isGone ? (200 + window.innerWidth) * xDir : active ? mx : 0; // When a card is gone it flys out left or right, otherwise goes back to zero
        const rot = mx / 100 + (isGone ? xDir * 10 * vx : 0); // How much the card tilts, flicking it harder makes it rotate faster
        const scale = active ? 1.1 : 1; // Active cards lift up a bit
        return {
          x,
          rot,
          scale,
          delay: undefined,
          config: { friction: 50, tension: active ? 800 : isGone ? 200 : 500 },
        };
      });
      if (!active && gone.size === count)
        setTimeout(() => {
          gone.clear();
          api.start((i) => to(i));
        }, 600);
    }
  );
  // Now we're just mapping the animated values to our view, that's it. Btw, this component only renders once. :-)
  return (
    <section
      style={{
        width: '100%',
        height: '100vh',
        overflow: 'hidden',
      }}
    >
      <div className={cx('container')}>
        <img
          className={cx('home-btn')}
          src={iconimg}
          onClick={home}
          alt="home"
        />
        <div className={cx('petal-img')}>
          {props.map(({ x, y, rot, scale }, i) => (
            <animated.div className={cx('deck')} key={i} style={{ x, y }}>
              {/* This is the card itself, we're binding our gesture to it (and inject its index so we know which is which) */}
              <animated.div
                className={cx('petal')}
                {...bind(i)}
                style={{
                  transform: interpolate([rot, scale], trans),
                }}
              >
                <img className={cx('petals')} src={petal} alt="petals" />
                <div className={cx('contents')}>
                  <div className={cx('petal-img')}>
                    <div className={cx('editor')}>
                      <div className={cx('header')}>
                        <div className={cx('nation')}>
                          {list[i].nation}
                          <img
                            className={cx('nationimg')}
                            src={list[i].nationImageUrlPath}
                            alt="preview"
                          />
                        </div>
                        <div className={cx('date')}>{list[i].createdDate}</div>
                        <div>
                          {setBlind(list[i].seq) ? null : (
                            <SirenImg
                              width={22}
                              onClick={() =>
                                openModal(list[i].seq, list[i].message)
                              }
                            />
                          )}
                        </div>
                      </div>
                      <div className={cx('scrollBar')}>
                        <div className={cx('textarea')}>
                          {setBlind(list[i].seq)
                            ? '신고 접수된 글입니다.'
                            : `${list[i].message}`}
                        </div>
                        <div className={cx('thumbnail')}>
                          <div className={cx('default')}>
                            {setBlind(list[i].seq) ? null : list[i]
                                .contentImageUrlPath ? (
                              <div className={cx('preview-img')}>
                                <img
                                  src={list[i].contentImageUrlPath}
                                  alt="preview"
                                />
                              </div>
                            ) : null}
                          </div>
                        </div>
                      </div>
                    </div>
                  </div>
                </div>
              </animated.div>
            </animated.div>
          ))}
        </div>
        <ReportModal
          open={modalOpen}
          seq={petalseq}
          close={closeModal}
          complete={reportComplete}
          contents={msg}
        />
        <button className={cx('send-btn')} onClick={send}>
          Send
        </button>
      </div>
    </section>
  );
};

export default ContentsList;

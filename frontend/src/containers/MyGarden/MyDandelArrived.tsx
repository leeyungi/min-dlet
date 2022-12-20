import React, { useEffect, useState } from 'react';
import { useSprings, animated, to as interpolate } from '@react-spring/web';
import petal from 'assets/images/petal-yellow-4.png';
import { useDrag } from '@use-gesture/react';
import classNames from 'classnames/bind';
import Swal from 'sweetalert2';
import { getDandelionDetail, deletePetalSeq } from 'services/api/MyGardenApi';
import styles from './MyGardenDandelionDetail.module.scss';
import bin2 from 'assets/images/bin2.png';
import { useNavigate, useParams } from 'react-router-dom';
const cx = classNames.bind(styles);
const cards = [petal, petal, petal, petal, petal, petal];

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

const MyGardenDandelionDetail = () => {
  const { id } = useParams();
  const navigate = useNavigate();
  const [count, setCount] = useState(0);
  const [list, setList] = useState([
    {
      contentImageUrlPath: '',
      createdDate: '',
      message: '',
      nation: '',
      nationImageUrlPath: '',
      seq: 0,
    },
  ]);

  const [gone] = useState(() => new Set());
  const [props, api] = useSprings(count, (i) => ({
    ...to(i),
    from: from(i),
  }));

  const Back = () => {
    navigate('/mygarden');
  };

  const deletePetal = async (seq: number) => {
    Swal.fire({
      title: '해당 꽃잎을 삭제하시겠습니까?',
      showCancelButton: true,
      confirmButtonText: 'Yes',
    }).then(async (result) => {
      if (result.isConfirmed) {
        console.log(seq);
        const result = await deletePetalSeq(seq);
        if (result.status === 204) {
          Swal.fire(`삭제가 완료되었습니다.`, '', 'success');
          window.location.reload();
        }
      }
    });
    console.log(seq);
  };

  const getDetail = async () => {
    const num = Number(id);
    console.log(num);

    const response = await getDandelionDetail(num);
    console.log(response);
    setList(response.data.data.petalInfos.reverse());
    console.log(response.data.data.totalPetalCount);
    setCount(response.data.data.totalPetalCount);
  };
  useEffect(() => {
    getDetail();
  }, []);

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

  return (
    <section
      style={{
        width: '100%',
        height: '100vh',
        overflow: 'hidden',
      }}
    >
      <div className={cx('container')}>
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
                <img
                  className={cx('petals')}
                  src={`${cards[0]}`}
                  alt="petals"
                />
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
                        <div className={cx('date')}> {list[i].createdDate}</div>{' '}
                        <div>
                          {i !== count - 1 ? (
                            <img
                              className={cx('bin-img')}
                              src={bin2}
                              alt="bin"
                              onClick={() => deletePetal(list[i].seq)}
                            />
                          ) : null}
                        </div>
                      </div>
                      <div className={cx('scrollBar')}>
                        <div className={cx('textarea')}>{list[i].message}</div>
                        <div className={cx('thumbnail')}>
                          <div className={cx('default')}>
                            {list[i].contentImageUrlPath ? (
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
                      {/* {i + 1} */}
                    </div>
                  </div>
                </div>
              </animated.div>
            </animated.div>
          ))}
        </div>
        <div className={cx('send-btn')} onClick={Back}>
          Back
        </div>
      </div>
    </section>
  );
};
export default MyGardenDandelionDetail;

import styled from 'styled-components';
import sign from 'assets/images/signimg1.png';
import flower from 'assets/images/flower.png';
import { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import classNames from 'classnames/bind';
import styles from './MyGardenDandelion2.module.scss';
import bin2 from 'assets/images/bin4.png';
import photo from 'assets/images/photo-album.png';
import shovel from 'assets/images/shovel.png';
import pencil_check from 'assets/images/pencil_check.png';
import axios from 'axios';
import Swal from 'sweetalert2';

const cx = classNames.bind(styles);
const BaseURL = process.env.REACT_APP_BASE_URL;

const Sign = styled.div`
  color: white;
  font-weight: 800;
  font-size: 20px;
  position: relative;
  img {
    position: relative;
    height: 190px;
    width: 190px;
  }
`;

const Blossom = styled.div`
  img {
    position: absolute;
    top: 70px;
    max-height: 110px;
    max-width: 110px;
  }
`;

const ReturnedSign = styled.div`
  color: white;
  font-size: 20px;
  position: relative;
  animation: shake 1.5s;
  animation-iteration-count: infinite;
  @keyframes shake {
    0% {
      transform: translate(1px, 1px) rotate(0deg);
    }
    10% {
      transform: translate(-1px, -2px) rotate(-1deg);
    }
    20% {
      transform: translate(-3px, 0px) rotate(1deg);
    }
    30% {
      transform: translate(3px, 2px) rotate(0deg);
    }
    40% {
      transform: translate(1px, -1px) rotate(1deg);
    }
    50% {
      transform: translate(-1px, 2px) rotate(-1deg);
    }
    60% {
      transform: translate(-3px, 1px) rotate(0deg);
    }
    70% {
      transform: translate(3px, 1px) rotate(-1deg);
    }
    80% {
      transform: translate(-1px, -1px) rotate(1deg);
    }
    90% {
      transform: translate(1px, 2px) rotate(0deg);
    }
    100% {
      transform: translate(1px, -2px) rotate(-1deg);
    }
  }
  img {
    position: relative;
    height: 190px;
    width: 190px;
  }
`;

const IconBox = styled.div`
  display: flex;
  text-align: center;
`;

const IconCover = styled.div`
  display: flex;
  background-color: rgba(235, 235, 235, 0.85);
  border-radius: 50%;
  width: 40px;
  height: 40px;
  justify-content: center;
  align-items: center;
  margin-right: 5px;
`;

const Icons = styled.img`
  width: 30px !important;
  height: 30px !important;
`;

const Blank = styled.div`
  height: 40px;
`;

const Dday = styled.span`
  position: absolute;
  left: calc(max(7vw, 10px));
  // top: calc(max(10%, 25px));
  top: calc(max(15%, 10px));
  font-size: 20px;
`;

const AfterDday = styled.span`
  position: absolute;
  left: calc(max(4.5vw, 10px));
  // top: calc(max(10%, 25px));
  top: calc(max(15%, 10px));
  font-size: 15px;
`;

function MyGardenDandelion3({ dandelion, setDandel }) {
  // setDandel: ????????? ????????? ???????????? ?????????.
  const [show, setShow] = useState(false); // ???????????? show
  const [record, setRecord] = useState(false); // ?????? ?????????
  const [date, setDate] = useState('');
  const [finalDate, setFinalDate] = useState(dandelion.description);
  const [hasMessage, setHasMessage] = useState(false);
  const [status, setStatus] = useState(dandelion.status);
  const [returned, setReturned] = useState(false); // returned. ?????? ?????????!
  const [blossom, setBlossom] = useState(false);
  const [randomNum] = useState(Math.floor(Math.random() * 4 + 1));
  const navigate = new useNavigate();
  function getDiff(targetDate) {
    const today = new Date();
    const koreaTimeDiff = 9 * 60 * 60 * 1000;
    const dDay = new Date(targetDate);

    const diff = Math.floor(
      (dDay.getTime() + koreaTimeDiff - today.getTime()) / (1000 * 60 * 60 * 24)
    );
    if (diff <= 0) {
      setDate(`D - day`);
    } else {
      setDate(`D - ${diff}`);
    }
    // console.log(`Dday?????? ${dDay < 10 ? `0${diff}` : diff}??? ???????????????.`);
  }

  // onClick ============================================
  const onOptionsClick = () => {
    setShow((prev) => !prev);
  };

  const getList = (dandelionId) => {
    navigate(`dandelions/${dandelionId}`);
  };

  const onRecordClick = (dandelionId) => {
    Swal.fire({
      title: '????????? ???????????????',
      input: 'text',
      inputAttributes: {
        autocapitalize: 'off',
      },
      showCancelButton: true,
      confirmButtonText: '??????',
      cancelButtonText: '??????',
      showLoaderOnConfirm: true,
    }).then((res) => {
      if (res.isConfirmed) {
        registerDescription(dandelionId, res.value);
      }
    });
  };

  const onAlbumClick = (dandelionId) => {
    Swal.fire({
      title: '???????????? ?????? ???????????????????',
      showCancelButton: true,
      confirmButtonText: '??????',
      cancelButtonText: '??????',
    }).then((res) => {
      if (res.isConfirmed) {
        if (!hasMessage) {
          Swal.fire({
            title: '????????? ?????? ??????????????????!',
            confirmButtonText: '??????',
          });
        } else {
          saveDandelion(dandelionId);
        }
      }
    });
  };

  const onPlantClick = (dandelionId) => {
    Swal.fire({
      title: '????????? ????????????????',
      showCancelButton: true,
      confirmButtonText: '??????',
      cancelButtonText: '??????',
    }).then((res) => {
      if (res.isConfirmed) {
        plantDandelion(dandelionId);
      }
    });
  };

  const onDeleteClick = (dandelionId) => {
    Swal.fire({
      title: '???????????? ?????? ???????????????????',
      showCancelButton: true,
      confirmButtonText: '??????',
      cancelButtonText: '??????',
    }).then((res) => {
      if (res.isConfirmed) {
        deleteDandelion(dandelionId);
        setTimeout(() => {
          window.location.reload();
        }, 1000);
      }
    });
  };

  // async function ============================================
  async function registerDescription(dandelionId, description) {
    const token = localStorage.getItem('token');
    const config = {
      Authorization: 'Bearer ' + token,
    };
    await axios({
      url: `dandelions/${dandelionId}/description`,
      method: 'patch',
      data: { description: description },
      baseURL: BaseURL,
      headers: config,
    })
      .then((res) => {
        setFinalDate(description);
        Swal.fire({
          title: `[ ${description} ]??? ?????????????????????.`,
          confirmButtonText: '??????',
        });
        setHasMessage(true);
        console.log('?????? ?????? ?????? ??????');
      })
      .catch((err) => {
        Swal.fire({
          title: `?????? ????????? ??????????????????.`,
          confirmButtonText: '??????',
        });

        console.log('?????? ?????? ?????? ?????? ??????');
        console.log(err);
      });
  }

  async function saveDandelion(dandelionId) {
    const token = localStorage.getItem('token');
    const config = {
      Authorization: 'Bearer ' + token,
    };
    await axios({
      url: `dandelions/${dandelionId}/status`,
      method: 'patch',
      data: {
        status: 'ALBUM',
      },
      baseURL: BaseURL,
      headers: config,
    })
      .then((res) => {
        Swal.fire('???????????? ?????? ??????!', '', 'success');
        console.log('???????????? ?????? ??????');
        setStatus('ALBUM');
        setDandel(false);
      })
      .catch((err) => {
        Swal.fire('???????????? ?????? ??????!', '', 'warning');
        console.log('???????????? ?????? ??????');
        console.log(err);
      });
  }

  async function plantDandelion(dandelionId) {
    const token = localStorage.getItem('token');
    const config = {
      Authorization: 'Bearer ' + token,
    };
    await axios({
      url: `dandelions/${dandelionId}/status`,
      method: 'patch',
      data: {
        status: 'BLOSSOMED',
      },
      baseURL: BaseURL,
      headers: config,
    })
      .then((res) => {
        Swal.fire('?????? ?????? ??????!', '', 'success').then((res) => {
          if (res.isConfirmed) {
            console.log('?????? ?????? ??????');

            setRecord(true);
            setReturned(false);
            setBlossom(true);
            setStatus('BLOSSOMED');
            navigate(`/mygarden/plant/${dandelionId}`);
          }
        });
      })
      .catch((err) => {
        Swal.fire('?????? ?????? ??????!', '', 'warning');
        console.log('?????? ?????? ??????');
        console.log(err);
      });
  }

  async function deleteDandelion(dandelionId) {
    const token = localStorage.getItem('token');
    const config = {
      Authorization: 'Bearer ' + token,
    };
    await axios({
      url: `dandelions/${dandelionId}`,
      method: 'delete',
      baseURL: BaseURL,
      headers: config,
    })
      .then((res) => {
        Swal.fire('????????? ?????? ??????!', '', 'success');
        console.log('????????? ?????? ??????');
      })
      .catch((err) => {
        Swal.fire('????????? ?????? ??????!', '', 'warning');
        console.log('????????? ?????? ??????');
        console.log(err);
      });
  }
  useEffect(() => {
    // console.log(dandelion.flowerSignNumber);
    getDiff(dandelion.blossomedDate);
    if (finalDate == null) {
      setFinalDate('?????? ????????????');
    } else {
      setHasMessage(true);
    }
    // console.log('?????? ???', status, dandelion);
    if (status === 'FLYING' || status === 'ALBUM') {
      setRecord(false);
    } else if (status === 'RETURN') {
      setRecord(true);
      setReturned(true);
    } else if (status === 'BLOSSOMED') {
      setRecord(true);
      setReturned(false);
      setBlossom(true);
    }
  }, []);

  return (
    <div className={cx('container')}>
      <div className={cx('icons')} onClick={onOptionsClick}>
        {/* {show && <IconCover>
            <Icons src={cancel} alt="??????" />
          </IconCover>} */}

        {show && record && blossom && (
          <IconCover
            onClick={() => {
              onRecordClick(dandelion.seq);
            }}
          >
            <Icons src={pencil_check} alt="??????" />
          </IconCover>
        )}

        {show && record && blossom && (
          <IconCover
            onClick={() => {
              onAlbumClick(dandelion.seq);
            }}
          >
            <Icons src={photo} alt="?????????" />
          </IconCover>
        )}

        {show && returned && (
          <IconCover
            onClick={() => {
              onPlantClick(dandelion.seq);
            }}
          >
            <Icons src={shovel} alt="?????? ??????" />
          </IconCover>
        )}

        {show && (
          <IconCover
            onClick={() => {
              onDeleteClick(dandelion.seq);
            }}
          >
            <Icons src={bin2} alt="??????" />
          </IconCover>
        )}
      </div>
      <div onClick={onOptionsClick} className={cx('sign-flower')}>
        {returned ? (
          <div className={cx('returned-sign')}>
            <img
              style={{ width: '100%' }}
              src={require(`assets/images/signimg${dandelion.flowerSignNumber}.png`)}
              alt="??????"
            />
            <Dday>{date}</Dday>
          </div>
        ) : (
          <div className={cx('normal-sign')}>
            <img
              style={{ width: '100%' }}
              src={require(`assets/images/signimg${dandelion.flowerSignNumber}.png`)}
              alt="??????"
            />
            {blossom ? <AfterDday>{finalDate}</AfterDday> : <Dday>{date}</Dday>}
          </div>
        )}
        {blossom && (
          <Blossom>
            <img
              src={require(`assets/images/MyGarden/dand${randomNum}_garden.png`)}
              alt="???"
              onClick={() => getList(dandelion.seq)}
            />
          </Blossom>
        )}
      </div>
    </div>
  );
}

export default MyGardenDandelion3;

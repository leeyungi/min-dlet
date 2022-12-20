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
  // setDandel: 앨범에 넣으면 없애주기 위하여.
  const [show, setShow] = useState(false); // 아이콘들 show
  const [record, setRecord] = useState(false); // 연필 아이콘
  const [date, setDate] = useState('');
  const [finalDate, setFinalDate] = useState(dandelion.description);
  const [hasMessage, setHasMessage] = useState(false);
  const [status, setStatus] = useState(dandelion.status);
  const [returned, setReturned] = useState(false); // returned. 삽도 보여줌!
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
    // console.log(`Dday까지 ${dDay < 10 ? `0${diff}` : diff}일 남았습니다.`);
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
      title: '꽃말을 입력하세요',
      input: 'text',
      inputAttributes: {
        autocapitalize: 'off',
      },
      showCancelButton: true,
      confirmButtonText: '등록',
      cancelButtonText: '취소',
      showLoaderOnConfirm: true,
    }).then((res) => {
      if (res.isConfirmed) {
        registerDescription(dandelionId, res.value);
      }
    });
  };

  const onAlbumClick = (dandelionId) => {
    Swal.fire({
      title: '보관함에 저장 하시겠습니까?',
      showCancelButton: true,
      confirmButtonText: '저장',
      cancelButtonText: '취소',
    }).then((res) => {
      if (res.isConfirmed) {
        if (!hasMessage) {
          Swal.fire({
            title: '꽃말을 먼저 등록해주세요!',
            confirmButtonText: '확인',
          });
        } else {
          saveDandelion(dandelionId);
        }
      }
    });
  };

  const onPlantClick = (dandelionId) => {
    Swal.fire({
      title: '씨앗을 심겠습니까?',
      showCancelButton: true,
      confirmButtonText: '심기',
      cancelButtonText: '취소',
    }).then((res) => {
      if (res.isConfirmed) {
        plantDandelion(dandelionId);
      }
    });
  };

  const onDeleteClick = (dandelionId) => {
    Swal.fire({
      title: '민들레를 삭제 하시겠습니까?',
      showCancelButton: true,
      confirmButtonText: '삭제',
      cancelButtonText: '취소',
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
          title: `[ ${description} ]를 등록하였습니다.`,
          confirmButtonText: '확인',
        });
        setHasMessage(true);
        console.log('팻말 꽃말 등록 성공');
      })
      .catch((err) => {
        Swal.fire({
          title: `꽃말 등록을 실패했습니다.`,
          confirmButtonText: '확인',
        });

        console.log('팻말 꽃말 등록 성공 실패');
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
        Swal.fire('보관함에 저장 성공!', '', 'success');
        console.log('보관함에 저장 성공');
        setStatus('ALBUM');
        setDandel(false);
      })
      .catch((err) => {
        Swal.fire('보관함에 저장 실패!', '', 'warning');
        console.log('보관함에 저장 실패');
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
        Swal.fire('씨앗 심기 성공!', '', 'success').then((res) => {
          if (res.isConfirmed) {
            console.log('씨앗 심기 성공');

            setRecord(true);
            setReturned(false);
            setBlossom(true);
            setStatus('BLOSSOMED');
            navigate(`/mygarden/plant/${dandelionId}`);
          }
        });
      })
      .catch((err) => {
        Swal.fire('씨앗 심기 실패!', '', 'warning');
        console.log('씨앗 심기 실패');
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
        Swal.fire('민들레 삭제 완료!', '', 'success');
        console.log('민들레 삭제 성공');
      })
      .catch((err) => {
        Swal.fire('민들레 삭제 실패!', '', 'warning');
        console.log('민들레 삭제 실패');
        console.log(err);
      });
  }
  useEffect(() => {
    // console.log(dandelion.flowerSignNumber);
    getDiff(dandelion.blossomedDate);
    if (finalDate == null) {
      setFinalDate('꽃말 등록하기');
    } else {
      setHasMessage(true);
    }
    // console.log('이번 것', status, dandelion);
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
            <Icons src={cancel} alt="취소" />
          </IconCover>} */}

        {show && record && blossom && (
          <IconCover
            onClick={() => {
              onRecordClick(dandelion.seq);
            }}
          >
            <Icons src={pencil_check} alt="꽃말" />
          </IconCover>
        )}

        {show && record && blossom && (
          <IconCover
            onClick={() => {
              onAlbumClick(dandelion.seq);
            }}
          >
            <Icons src={photo} alt="보관함" />
          </IconCover>
        )}

        {show && returned && (
          <IconCover
            onClick={() => {
              onPlantClick(dandelion.seq);
            }}
          >
            <Icons src={shovel} alt="씨앗 심기" />
          </IconCover>
        )}

        {show && (
          <IconCover
            onClick={() => {
              onDeleteClick(dandelion.seq);
            }}
          >
            <Icons src={bin2} alt="삭제" />
          </IconCover>
        )}
      </div>
      <div onClick={onOptionsClick} className={cx('sign-flower')}>
        {returned ? (
          <div className={cx('returned-sign')}>
            <img
              style={{ width: '100%' }}
              src={require(`assets/images/signimg${dandelion.flowerSignNumber}.png`)}
              alt="팻말"
            />
            <Dday>{date}</Dday>
          </div>
        ) : (
          <div className={cx('normal-sign')}>
            <img
              style={{ width: '100%' }}
              src={require(`assets/images/signimg${dandelion.flowerSignNumber}.png`)}
              alt="팻말"
            />
            {blossom ? <AfterDday>{finalDate}</AfterDday> : <Dday>{date}</Dday>}
          </div>
        )}
        {blossom && (
          <Blossom>
            <img
              src={require(`assets/images/MyGarden/dand${randomNum}_garden.png`)}
              alt="꽃"
              onClick={() => getList(dandelion.seq)}
            />
          </Blossom>
        )}
      </div>
    </div>
  );
}

export default MyGardenDandelion3;

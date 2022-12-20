import styled from 'styled-components';
import sign from 'assets/images/signimg.png';
import flower from 'assets/images/flower.png';
import { useEffect, useState } from 'react';
import classNames from 'classnames/bind';
import styles from './MyGardenDandelion.module.scss';
import cancel from 'assets/images/cancel.png';
import photo from 'assets/images/photo-album.png';
import shovel from 'assets/images/shovel.png';
import flower_scissors from 'assets/images/flower_scissors.png';
import pencil_check from 'assets/images/pencil_check.png';
import axios from 'axios';
import Swal from 'sweetalert2';

const cx = classNames.bind(styles);
const BaseURL = process.env.REACT_APP_BASE_URL;

const Sign = styled.div`
  color: white;
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
    top: 50px;
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
  background-color: white;
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
  left: 37px;
  top: 20px;
  font-size: 20px;
`;

function MyGardenDandelion2({ dandelion }) {
  const [show, setShow] = useState(false);
  const [showPlant, setShowPlant] = useState(false);
  const [record, setRecord] = useState(false);
  const [date, setDate] = useState('');
  const [status, setStatus] = useState(dandelion.status);
  const [returned, setReturned] = useState(false);
  const [blossom, setBlossom] = useState(false);

  // function ============================================
  function getDiff(targetDate) {
    const today = new Date();
    const dDay = new Date(targetDate);
    const diff = Math.floor(
      (dDay.getTime() - today.getTime()) / (1000 * 60 * 60 * 24)
    );
    setDate(`D - ${diff}`);
    // console.log(`Dday까지 ${dDay < 10 ? `0${diff}` : diff}일 남았습니다.`);
  }

  // onClick ============================================
  const onOptionsClick = () => {
    setShow((prev) => !prev);
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
        saveDandelion(dandelionId);
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
        Swal.fire({
          title: `[ ${description} ]를 등록하였습니다.`,
          confirmButtonText: '확인',
        });
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
        Swal.fire('씨앗 심기 성공!', '', 'success');
        console.log('씨앗 심기 성공');
        setStatus('BLOSSOMED');
        setReturned(false);
        setBlossom(true);
        console.log('==========');
        console.log(status);
        console.log('==========');
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
    console.log('블라썸 확인');
    console.log(blossom + `${dandelion.seq}`);

    getDiff(dandelion.blossomedDate);
    // setStatus(dandelion.status);
    if (dandelion.status === 'FLYING' || dandelion.status === 'HOLD') {
      setRecord(false);
    } else if (dandelion.status === 'RETURN') {
      setRecord(true);
      setReturned(true);
      setShowPlant(true);
    } else if (dandelion.status === 'BLOSSOMED') {
      setRecord(true);
      setReturned(false);
      setShowPlant(false);
      setBlossom(true);
    }
  }, [status]);
  return (
    <div className={cx('container')}>
      <div onClick={onOptionsClick}>
        {show ? (
          <div>
            <IconBox>
              <IconCover>
                <Icons src={cancel} alt="취소" />
              </IconCover>

              {record && blossom ? (
                <IconCover
                  onClick={() => {
                    onRecordClick(dandelion.seq);
                  }}
                >
                  <Icons src={pencil_check} alt="꽃말" />
                </IconCover>
              ) : (
                <span></span>
              )}

              {record && blossom ? (
                <IconCover
                  onClick={() => {
                    onAlbumClick(dandelion.seq);
                  }}
                >
                  <Icons src={photo} alt="보관함" />
                </IconCover>
              ) : (
                <span></span>
              )}

              {showPlant ? (
                <IconCover
                  onClick={() => {
                    onPlantClick(dandelion.seq);
                  }}
                >
                  <Icons src={shovel} alt="씨앗 심기" />
                </IconCover>
              ) : (
                <span></span>
              )}

              <IconCover
                onClick={() => {
                  onDeleteClick(dandelion.seq);
                }}
              >
                <Icons src={flower_scissors} alt="삭제" />
              </IconCover>
            </IconBox>
            <div>
              <Sign>
                <img src={sign} alt="팻말" />
                <Dday>{date}</Dday>
                {blossom ? (
                  <Blossom>
                    <img src={flower} alt="꽃" />
                  </Blossom>
                ) : (
                  <div></div>
                )}
              </Sign>
            </div>
          </div>
        ) : (
          <div>
            <Blank></Blank>
            {returned ? (
              <ReturnedSign>
                <img src={sign} alt="팻말" />
                <Dday>{date}</Dday>
              </ReturnedSign>
            ) : (
              <Sign>
                <img src={sign} alt="팻말" />
                <Dday>{date}</Dday>
                {/* <span>{dandelion.status}</span> */}
                {blossom ? (
                  <Blossom>
                    <img src={flower} alt="꽃" />
                  </Blossom>
                ) : (
                  <div></div>
                )}
              </Sign>
            )}
          </div>
        )}
      </div>
    </div>
  );
}

export default MyGardenDandelion2;

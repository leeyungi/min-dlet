import styles from "./MyAlbum.module.scss";
import classNames from "classnames/bind";
import axios from "axios";
import { useEffect, useState } from "react";
import MyAlbumFlower from "./MyAlbumFlower";

const cx = classNames.bind(styles);
const BaseURL = process.env.REACT_APP_BASE_URL;

function MyAlbumSide({ order, totalPage }) {
  const [flowers, setFlowers] = useState([])

  const token = localStorage.getItem("token");
  const config = {
    Authorization: "Bearer " + token,
  };

  useEffect(() => {
    axios({
      // url: `baseUrl/dandelions/{id}/description`, 나중에 아이디 있는거로 교체
      url: `garden/album?page=${order}&size=4`,
      method: "GET",
      baseURL: BaseURL,
      headers: config,
    }).then((res) => {
      if (res.status === 200) {
        setFlowers(res.data.data.dandelionInfos)
      }
    })
  }, [])


  return (
    <>
      <input style={{width: "20px", height: "20px"}} type="radio" id={`page-${order}`}/>
      <div className={cx('book__page')} style={{zIndex: 100 - order}}>
        <div className={cx('book__page__content')}>
          {flowers.map((value, idx) => {
            return <MyAlbumFlower key={idx}
            description={value.description} 
            dandelionSeq={value.dandelionSeq} />
          })}
          {/* <div className={cx('book__page__content__flower')}>
            <span>🌼꽃</span>
          </div>
          <div className={cx('book__page__content__flower')}>
            <span>🌼꽃</span>
          </div>
          <div className={cx('book__page__content__flower')}>
            <span>🌼꽃</span>
          </div>
          <div className={cx('book__page__content__flower')}>
            <span>🌼꽃</span>
          </div> */}
        </div>
        <span style={{position: "absolute", fontSize: "1.2rem", bottom: "10px", right: "15px"}}>{order} / {totalPage}</span>
      </div>
    </>

  );
}
export default MyAlbumSide;

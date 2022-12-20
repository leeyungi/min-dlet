import React, { useEffect } from 'react';
import { useRecoilState } from 'recoil';
import { useLocation } from 'react-router-dom';
import { useNavigate } from 'react-router';
import memberState from 'utils/memberState';
import axios from 'axios';

const BaseURL = process.env.REACT_APP_BASE_URL

// 로그인하고 recoil state member를 만드는 함수
function LoginRecoil() {
  const navigate = useNavigate()
  const location = useLocation()
  const [, setMember] = useRecoilState(memberState)

  const parseJwt = (token: string) => {
    try {
      // return Buffer.from(token, 'base64')
      return JSON.parse(atob(token.split('.')[1]));
    } catch (e) {
      return null;
    }
  };


  useEffect(() => {
    const token = localStorage.getItem('token')
    if (!token) {
      if (!['/login', '/signup'].includes(location.pathname)) {
        navigate('/login')
      }
      return
    } else {
      const seq = parseJwt(token).seq
      // const checkData = {
      //   method: 'GET',
      //   headers: {
      //     'Authorization': `Bearer ${token}`,
      //     'Content-Type': 'application/json'
      //   }
      // }

      axios({
        // url: `baseUrl/dandelions/{id}/description`, 나중에 아이디 있는거로 교체
        url: `members/${seq}`,
        method: "GET",
        baseURL: BaseURL,
        headers: {
          Authorization: "Bearer " + token,
        },})
        .then((result) => {
          if (result.status !== 200) {
            localStorage.removeItem('token')
            navigate('/login')
            return
          }
          const data = result.data.data
          setMember(data)
        })
        .catch((err) => {
          localStorage.removeItem('token')
          navigate('/login')
          return
          console.error(err)
        } )

      // 회원정보 조회
      // fetch(`${BaseURL}members`, checkData)
      //   .then((res) => res.json())
      //   .then((result) => {
      //     if (result.status !== 200) {
      //       localStorage.removeItem('token')
      //       navigate('/login')
      //       return
      //     }
      //     const data = result.data
      //     setMember(data)
      //   })
      //   .catch((err) => {console.error(err)} )
  }}, [])


  return null;
}

export default LoginRecoil;
import { atom } from 'recoil';

export const isLoggedState = atom({
  key: 'isLoggedState',
  default: false,
});

/* 프로필 정보 요청 */
export const loggedUserState = atom({
  key: 'loggedUserState',
  default: [],
});

/* 꽃잎 잡기 */
export const petalCatchResultList = atom({
  key: 'petalCatchResult',
  default: [{}],
});

export const petalCatchResultSeq = atom({
  key: 'petalCatchResultSeq',
  default: 0,
});

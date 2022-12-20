import { atom } from 'recoil'

const memberState = atom({
  key: 'memberState',
  default: {
    language: 'KOREAN',
    id: '익명',
    community: 'KOREA',
    soundOff: true,
    role: 'MEMBER',
    seq: 1
  }
})

export default memberState
import { atom } from 'recoil'

const audioState = atom({
  key: 'audioState',
  default: {
    flowerGarden : false,
    landing : false,
    stopFunc: () => {},
  }
})

export default audioState
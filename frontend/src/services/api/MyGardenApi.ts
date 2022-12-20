import instance from 'services/axios';

// // 꽃밭 보기
// export function getGarden() {
//   return fetch(`${BASE_PATH}/garden`).then((response) => response.json());
// }

// 민들레 상세정보
export const getDandelionDetail = async (dandelionSeq: number) => {
  const result = await instance.get(`dandelions/${dandelionSeq}`);
  return result;
};

// 꽃잎 삭제
export const deletePetalSeq = async (petalSeq: number) => {
  const result = await instance.delete(`petals/${petalSeq}`);
  return result;
};

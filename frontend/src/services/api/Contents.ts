import instance from 'services/axios';

const COMMON = '/dandelions';
export const leftSeedCount = async () => {
  const response = await instance.get(COMMON + '/leftover-seed-count');
  return response.data;
};

export const postContents = async (formData: any) => {
  const result = await instance.post('/dandelions', formData);
  return result;
};

export const postContentsAdd = async ({ formData, seq }: any) => {
  const result = await instance.post(COMMON + `/${seq}/petals`, formData);
  // console.log(result);
  return result;
};

export const getContents = async () => {
  const result = await instance.get(COMMON + '/random');
  return result;
};

export const resetContentsState = async (seq: number) => {
  const result = await instance.patch(COMMON + `/${seq}/status-flying`);
  return result;
};

export const reportContents = async ({ seq, reason }: any) => {
  const requestbody = { reason: reason };
  const result = await instance.post(`/petals/${seq}/reports`, requestbody);
  return result;
};

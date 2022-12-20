import React, { useEffect, useState } from 'react';
import classNames from 'classnames/bind';
import styles from './ContentsMain.module.scss';
import Plant from '../Plant/Plant';
import Blow from '../Blow/Blow';
import { petalCatchResultList, petalCatchResultSeq } from 'atoms/atoms';
import ContentsListing from '../ContentsListing/ContentsListing';
import ContentsEdit from '../ContentsEdit/ContentsEdit';
import ContentsCheck from '../ContentsCheck/ContentsCheck';
import { useNavigate } from 'react-router-dom';
import { useRecoilValue } from 'recoil';
import { getContents } from 'services/api/Contents';
import set from 'date-fns/fp/set/index';
const cx = classNames.bind(styles);

const ContentsMain = () => {
  const [formSteps, setFormStep] = useState(0);
  const [form, setForm] = useState({ image: '', message: '', date: '' });
  const [seq, setSeq] = useState(0);
  const [count, setCount] = useState(0);
  const navigate = useNavigate();
  const petaldata = useRecoilValue(petalCatchResultList);
  const patalseq = useRecoilValue(petalCatchResultSeq);
  const [list, setList] = useState([
    {
      // contentImageUrlPath: '',
      // createdDate: '',
      // message: '',
      // nation: '',
      // nationImageUrlPath: '',
      // seq: 0,
    },
  ]);

  useEffect(() => {
    if (patalseq === 0) {
      navigate('/');
    }
  }, [formSteps]);

  useEffect(() => {
    setList(petaldata);
    setSeq(patalseq);
    setCount(petaldata.length);
  }, []);

  const handleformStep = (step: number) => {
    setFormStep(step);
  };

  const handleFormSet = (data: any) => {
    setForm({ ...form, ...data });
  };

  const handleSeq = (data: number) => {
    setSeq(data);
  };

  const handleList = (data: any) => {
    setList(data);
  };

  const handleCount = (data: number) => {
    setCount(data);
  };

  return (
    <div>
      {(() => {
        switch (formSteps) {
          case 0:
            return (
              <Plant
                onClick={handleformStep}
                seq={handleSeq}
                count={handleCount}
                list={handleList}
              />
            );
          case 1:
            return (
              <ContentsListing
                seq={seq}
                list={list}
                count={count}
                onClick={handleformStep}
                form={form}
                setForm={handleFormSet}
              />
            );
          case 2:
            return (
              <ContentsEdit
                onClick={handleformStep}
                form={form}
                setForm={handleFormSet}
              />
            );
          case 3:
            return (
              <ContentsCheck
                onClick={handleformStep}
                form={form}
                setForm={handleFormSet}
              />
            );
          case 4:
            return (
              <>
                <Blow
                  seq={seq}
                  onClick={handleformStep}
                  form={form}
                  setForm={handleFormSet}
                />
              </>
            );
        }
      })()}
    </div>
  );
};

export default ContentsMain;

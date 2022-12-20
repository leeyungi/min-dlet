import React, { useEffect, useState } from 'react';
import classNames from 'classnames/bind';
import styles from './ContentsMain.module.scss';
import Plant from '../Plant/Plant';
import NewContentsEdit from '../NewContentsEdit/NewContentsEdit';
import ContentsCheck from '../ContentsCheck/ContentsCheck';
import Blow from '../Blow/Blow';
import ContentsSetDate from '../ContentsSetDate/ContentsSetDate';
import { getContents } from 'services/api/Contents';
import toast from 'react-hot-toast';
import { Navigate, useNavigate } from 'react-router-dom';
const cx = classNames.bind(styles);

const ContentsMain = () => {
  const [formSteps, setFormStep] = useState(0);
  const [form, setForm] = useState({
    image: '',
    message: '',
    date: '',
  });

  const handleformStep = (step: number) => {
    setFormStep(step);
  };

  const handleFormSet = (data: any) => {
    setForm({ ...form, ...data });
  };

  return (
    <div>
      {(() => {
        switch (formSteps) {
          case 0:
            return <Plant onClick={handleformStep} />;
          case 1:
            return (
              <NewContentsEdit
                onClick={handleformStep}
                form={form}
                setForm={handleFormSet}
              />
            );
          case 2:
            return (
              <ContentsCheck
                onClick={handleformStep}
                form={form}
                setForm={handleFormSet}
              />
            );
          case 3:
            return (
              <div>
                <ContentsSetDate
                  onClick={handleformStep}
                  form={form}
                  setForm={handleFormSet}
                />
              </div>
            );
          case 4:
            return (
              <div>
                <Blow
                  onClick={handleformStep}
                  form={form}
                  setForm={handleFormSet}
                />
              </div>
            );
        }
      })()}
    </div>
  );
};

export default ContentsMain;

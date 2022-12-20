import React, { useEffect, useRef, useState } from 'react';
import petal from 'assets/images/petal-yellow-4.png';
import classNames from 'classnames/bind';
import styles from './ContentsEditor.module.scss';
import { ReactComponent as ImageIcon } from 'assets/images/icon/uploadicon.svg';
import { ReactComponent as DeleteImg } from 'assets/images/icon/icon_img_delete.svg';
const cx = classNames.bind(styles);

const ContentsEditor = ({ form, img, msg, onSend }: any) => {
  const [letters, SetLetters] = useState(0);
  const [imgFile, setImgFile] = useState('');
  const [text, SetText] = useState<string>('');
  const date = new Date();
  date.setDate(date.getDate() + 2);
  const inputRef = useRef<HTMLInputElement>(null);
  const handleChange = (e: React.ChangeEvent<HTMLTextAreaElement>) => {
    SetText(e.target.value);
    msg(e.target.value);
  };
  const getByte = (str: string) => {
    const byte: number = str
      .split('')
      .map((s: string) => s.charCodeAt(0))
      .reduce((prev, c) => prev + (c === 10 ? 1 : c >> 7 ? 1 : 1), 0);
    if (byte <= 250) {
      SetLetters(byte);
    } else {
      // TODO : hot toast alert 적용 및 넘김 처리
      alert('초과!');
      SetText(text.substring(0, text.length - 1));
      msg(text.substring(0, text.length - 1));
    }
  };

  const handleUploadBtnClick = () => {
    inputRef.current?.click();
  };

  const handleUploadImage = async (event: any) => {
    const file = event.target.files;
    img(file[0]);
    setImgFile(URL.createObjectURL(file[0]));
  };

  const sendData = () => {
    onSend();
  };

  const deleteImg = () => {
    img('');
    setImgFile('');
  };

  useEffect(() => {
    getByte(text);
  }, [text]);

  useEffect(() => {
    SetText(form.message);
    if (form.image) setImgFile(URL.createObjectURL(form.image));
  }, []);

  return (
    <div
      className={cx('container')}
      style={{
        width: '100%',
        overflow: 'hidden',
      }}
    >
      <div className={cx('inner-container')}>
        <div className={cx('petal-img')}>
          <img className={cx('petal')} src={petal} alt="petal" />
          <div className={cx('editor')}>
            <textarea
              placeholder="마음을 전해 보세요🌼"
              value={text}
              onChange={handleChange}
            />
            <div className={cx('byte-limit')}>{letters}/250자 </div>

            <div className={cx('thumbnail')}>
              <div className={cx('default')} onClick={handleUploadBtnClick}>
                <input
                  type="file"
                  id="inputImage"
                  className={cx('upload-image')}
                  onChange={handleUploadImage}
                  ref={inputRef}
                  accept="image/*"
                />
                {imgFile ? (
                  <>
                    <div className={cx('preview-img')}>
                      <img src={imgFile} alt="preview" />
                    </div>
                  </>
                ) : (
                  <div className={cx('upload')}>
                    <ImageIcon width={42} height={42} />
                  </div>
                )}
              </div>
            </div>
            {imgFile ? (
              <div className={cx('delete')} onClick={deleteImg}>
                <DeleteImg width={25} height={25} />
              </div>
            ) : null}
          </div>
        </div>
        <div>
          {letters >= 1 || imgFile ? (
            <div className={cx('write-btn')} onClick={sendData}>
              Write
            </div>
          ) : (
            <div className={cx('minsize-msg')}>
              메세지 작성 또는 이미지를 첨부해주세요✉
            </div>
          )}
        </div>
      </div>
    </div>
  );
};

export default ContentsEditor;

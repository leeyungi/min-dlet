export const sound = async () => {
  const stream = await navigator.mediaDevices.getUserMedia({
    audio: true,
    video: false,
  });
  //let flag = false;
  const audioContext = new AudioContext();
  const mediaStreamAudioSourceNode =
    audioContext.createMediaStreamSource(stream);
  const analyserNode = audioContext.createAnalyser();
  mediaStreamAudioSourceNode.connect(analyserNode);

  const pcmData = new Float32Array(analyserNode.fftSize);

  const onFrame = () => {
    analyserNode.getFloatTimeDomainData(pcmData);
    let sumSquares = 0.0;
    for (const amplitude of pcmData) {
      sumSquares += amplitude * amplitude;
    }

    if (sumSquares > 100) {
      window.dispatchEvent(new CustomEvent('blow'));
      console.log(sumSquares);
      stream.getTracks()[0].stop();
    } else {
      window.requestAnimationFrame(onFrame);
    }
  };
  window.requestAnimationFrame(onFrame);
};

import React, { useState, Suspense, useEffect } from 'react';
import { Canvas } from '@react-three/fiber';
import { OrbitControls, Html } from '@react-three/drei';
import DandelionDown from '../DandelionDown';
import DandelionSeedUp from '../DandelionSeedUp';
import DandelionPetalDown from '../DandelionPetalDown';
import FlyingSeed from '../FlyingSeed';
import Grass from '../Grass';
import LoadingImg from 'assets/images/mindletGif.gif';
import FlyingSeedAnimation from '../FlyingSeedAnimation';

function FallbackTitle() {
  return (
    <Html center>
      <h1 style={{ color: 'white' }}>
        <br />
        <img src={LoadingImg} alt="loading" />
        <br />
      </h1>
    </Html>
  );
}

// function msgCompleteTitle() {
//   return (
//     <Html center>
//       <h1 style={{ color: 'white' }}>
//         <br />
//         메세지 전송이 완료되었습니다.
//         <br />
//       </h1>
//     </Html>
//   );
// }

export default function BlowAnimation({
  endstate,
  msgCheck,
  isPossible,
  windState,
  touchEvt,
}) {
  const [appear, SetAppear] = useState(false);
  const [nextstate, SetNext] = useState(false);
  const [seedState, SetSeed] = useState(false);
  const [dandelstate, SetDandel] = useState(false);
  const [petalstate, SetPetal] = useState(false);

  const detectReady = (detect) => {
    isPossible(true);
  };

  const appearState = (state) => {
    SetAppear(state);
  };

  const touch = () => {
    console.log(isPossible);
  };

  const handleNext = (next) => {
    SetNext(next);
  };

  const down = (next) => {
    SetSeed(next);
  };

  const DandelDown = (petals) => {
    SetDandel(petals);
  };

  const petalAppear = (petal) => {
    SetPetal(petal);
  };

  const touchEvent = (state) => {
    touchEvt(state);
  };

  useEffect(() => {
    if (nextstate === true) endstate(true);
  }, [nextstate]);

  useEffect(() => {
    if (petalstate === true) DandelDown(true);
  }, [petalstate]);

  useEffect(() => {
    if (seedState === true) console.log('민들레 내려감');
  }, [seedState]);

  return (
    <Canvas
      frameloop="demand"
      camera={{
        position: [60, -50, 110],
        fov: 90,
        far: 500,
        near: 10,
      }}
      onClick={touch}
    >
      <ambientLight intensity={0.3} />
      <Suspense fallback={<FallbackTitle />}>
        {!petalstate && <DandelionPetalDown flag={petalAppear} />}
        <directionalLight position={[0.5, 1, 0.866]} intensity={1.7} />
        <DandelionDown SeedUp={down} petal={petalstate} />
        {windState && <DandelionSeedUp flag={handleNext} />}
        <directionalLight position={[0.5, 10, 0.866]} intensity={1.7} />
        {!appear && (
          <FlyingSeedAnimation
            seedUp={seedState}
            wind={windState}
            ready={detectReady}
            appear={appearState}
            touch={touchEvent}
          />
        )}
        <directionalLight position={[-1, -0.3, -0.866]} intensity={1} />
      </Suspense>
      <OrbitControls
        enableRotate={false}
        enablePan={false}
        autoRotate={false}
        minDistance={120}
        maxDistance={150}
      />
    </Canvas>
  );
}

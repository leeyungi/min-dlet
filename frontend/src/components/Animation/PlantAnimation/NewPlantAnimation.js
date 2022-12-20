import React, { useState, Suspense, useEffect } from 'react';
import { Canvas } from '@react-three/fiber';
import { OrbitControls, Html } from '@react-three/drei';
import DandelionFlower from '../DandelionFlower';
import DandelionPetalUp from '../DandelionPetalUp';
import Dandelion from '../../Landing/Dandelion';
import LoadingImg from 'assets/images/mindletGif.gif';
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

export default function NewPlantAnimation({ endstate }) {
  const [nextstate, SetNext] = useState(0);

  const handleNext = (next) => {
    SetNext(next);
  };

  useEffect(() => {
    if (nextstate > 0) endstate(true);
  }, [nextstate]);

  return (
    <Canvas
      frameloop="demand"
      camera={{
        position: [60, -50, 110],
        // <Canvas frameloop="demand" style={{pointerEvents: 'auto', cursor:'pointer'}} pixelRatio={[1, 1]} camera={{ position: [-15, 27, 150],
        fov: 90,
        far: 500,
        near: 10,
      }}
    >
      <ambientLight intensity={0.3} />
      <Suspense fallback={<FallbackTitle />}>
        <DandelionFlower />
        <DandelionPetalUp flag={handleNext} />
        <directionalLight
          position={[0.5, 1, 0.866]}
          intensity={1.7}
          // castShadow
        />

        <directionalLight
          // color="#b6fccd"
          position={[0.5, 10, 0.866]}
          intensity={1.7}
          // castShadow
        />
        <directionalLight
          position={[-1, -0.3, -0.866]}
          intensity={1}
          // castShadow
        />
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

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "+00:00";

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

DROP DATABASE IF EXISTS `comanda_eletronica`;
CREATE DATABASE IF NOT EXISTS `comanda_eletronica` DEFAULT CHARACTER SET latin1 COLLATE latin1_swedish_ci;
USE `comanda_eletronica`;

DROP TABLE IF EXISTS `categoria`;
CREATE TABLE `categoria` (
  `idCategoria` int(11) NOT NULL,
  `nomeCategoria` varchar(200) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

INSERT INTO `categoria` (`idCategoria`, `nomeCategoria`) VALUES
(1, 'Bebidas'),
(2, 'Carnes'),
(3, 'Aves'),
(4, 'Frutos do mar'),
(5, 'Sobremesa');

DROP TABLE IF EXISTS `itempedido`;
CREATE TABLE `itempedido` (
  `idItemPedido` int(11) NOT NULL,
  `idProduto` int(11) NOT NULL,
  `qtdProduto` int(11) NOT NULL,
  `idPedido` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

INSERT INTO `itempedido` (`idItemPedido`, `idProduto`, `qtdProduto`, `idPedido`) VALUES
(76, 2, 2, 95),
(78, 7, 1, 95),
(79, 12, 7, 101),
(80, 12, 4, 104),
(81, 6, 1, 105),
(82, 6, 1, 106),
(83, 14, 4, 108);

DROP TABLE IF EXISTS `mesa`;
CREATE TABLE `mesa` (
  `idMesa` int(11) NOT NULL,
  `numeroMesa` int(11) NOT NULL,
  `statusMesa` varchar(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

INSERT INTO `mesa` (`idMesa`, `numeroMesa`, `statusMesa`) VALUES
(1, 1, 'livre'),
(2, 2, 'livre'),
(3, 3, 'livre'),
(4, 4, 'livre'),
(5, 5, 'livre'),
(6, 6, 'livre'),
(7, 7, 'livre'),
(8, 8, 'livre'),
(9, 9, 'livre'),
(10, 10, 'livre'),
(11, 11, 'livre'),
(12, 12, 'livre');

DROP TABLE IF EXISTS `pedido`;
CREATE TABLE `pedido` (
  `idPedido` int(11) NOT NULL,
  `idMesa` int(11) NOT NULL,
  `idUsuario` int(11) NOT NULL,
  `statusPedido` varchar(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

INSERT INTO `pedido` (`idPedido`, `idMesa`, `idUsuario`, `statusPedido`) VALUES
(95, 5, 1, 'concluido'),
(101, 5, 1, 'concluido'),
(104, 11, 1, 'concluido'),
(105, 1, 1, 'concluido'),
(106, 1, 1, 'concluido'),
(108, 1, 1, 'concluido');

DROP TABLE IF EXISTS `produto`;
CREATE TABLE `produto` (
  `idProduto` int(11) NOT NULL,
  `descricaoProduto` varchar(200) NOT NULL,
  `precoProduto` float NOT NULL,
  `idCategoria` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

INSERT INTO `produto` (`idProduto`, `descricaoProduto`, `precoProduto`, `idCategoria`) VALUES
(1, 'Carne de Sol Completa', 25, 2),
(2, 'Picanha Argentina Meia', 19.5, 2),
(6, 'Coca-Cola 2L', 9.5, 1),
(7, 'Bife acebolado', 17.9, 2),
(11, 'Frango Atropelado', 23, 3),
(12, 'Strogonoff de frango', 16, 3),
(13, 'Água mineral 510ml', 2, 1),
(14, 'Guaravita', 1.8, 1),
(15, 'Lula empanada', 37.4, 4),
(16, 'Panela de Frutos do Mar', 127.6, 4),
(17, 'Brigadeiro 18g (unidade)', 0.8, 5),
(18, 'Cajuzinho 18g (unidade)', 0.8, 5),
(19, 'Brownie de chocolate com cobertura chocolate meio amargo 50g', 5, 5),
(20, 'Trufa chocolate branco 50gr', 3, 5);

DROP TABLE IF EXISTS `usuario`;
CREATE TABLE `usuario` (
  `idUsuario` int(11) NOT NULL,
  `nomeUsuario` varchar(100) NOT NULL,
  `senhaUsuario` varchar(100) NOT NULL,
  `cargoUsuario` varchar(100) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

INSERT INTO `usuario` (`idUsuario`, `nomeUsuario`, `senhaUsuario`, `cargoUsuario`) VALUES
(1, 'atendimento', 'atendimento', 'atendimento'),
(2, 'cozinha', 'cozinha', 'cozinha');


ALTER TABLE `categoria`
  ADD PRIMARY KEY (`idCategoria`);

ALTER TABLE `itempedido`
  ADD PRIMARY KEY (`idItemPedido`),
  ADD KEY `idProduto` (`idProduto`),
  ADD KEY `idPedido` (`idPedido`);

ALTER TABLE `mesa`
  ADD PRIMARY KEY (`idMesa`);

ALTER TABLE `pedido`
  ADD PRIMARY KEY (`idPedido`),
  ADD KEY `idMesa` (`idMesa`),
  ADD KEY `idUsuario` (`idUsuario`);

ALTER TABLE `produto`
  ADD PRIMARY KEY (`idProduto`),
  ADD KEY `idCategoria` (`idCategoria`);

ALTER TABLE `usuario`
  ADD PRIMARY KEY (`idUsuario`);


ALTER TABLE `categoria`
  MODIFY `idCategoria` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;

ALTER TABLE `itempedido`
  MODIFY `idItemPedido` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=84;

ALTER TABLE `mesa`
  MODIFY `idMesa` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=13;

ALTER TABLE `pedido`
  MODIFY `idPedido` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=109;

ALTER TABLE `produto`
  MODIFY `idProduto` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=21;

ALTER TABLE `usuario`
  MODIFY `idUsuario` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;


ALTER TABLE `itempedido`
  ADD CONSTRAINT `itempedido_ibfk_1` FOREIGN KEY (`idProduto`) REFERENCES `produto` (`idProduto`) ON DELETE CASCADE,
  ADD CONSTRAINT `itempedido_ibfk_2` FOREIGN KEY (`idPedido`) REFERENCES `pedido` (`idPedido`) ON DELETE CASCADE;

ALTER TABLE `pedido`
  ADD CONSTRAINT `pedido_ibfk_1` FOREIGN KEY (`idMesa`) REFERENCES `mesa` (`idMesa`) ON DELETE CASCADE,
  ADD CONSTRAINT `pedido_ibfk_2` FOREIGN KEY (`idUsuario`) REFERENCES `usuario` (`idUsuario`) ON DELETE CASCADE;

ALTER TABLE `produto`
  ADD CONSTRAINT `produto_ibfk_1` FOREIGN KEY (`idCategoria`) REFERENCES `categoria` (`idCategoria`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
